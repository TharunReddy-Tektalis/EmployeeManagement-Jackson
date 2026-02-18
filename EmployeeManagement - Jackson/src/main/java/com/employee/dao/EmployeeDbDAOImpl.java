package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.employee.enums.EMSLoginResult;
import com.employee.enums.EMSRoles;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.ValidationException;
import com.employee.model.EmpLoginResult;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class EmployeeDbDAOImpl implements EmployeeDAO {

	private static final String checkEmpQuery = "select 1 from empData where empId = ?";

	private static final String addIntoEmpData = "INSERT INTO EmpData (empName, empDept, empDOB, empAddress, empEmail) VALUES (?,?, ?, ?, ?)";
	private static final String addIntoEmpLogin = "INSERT INTO EmpLogin (empId, empPass) VALUES (?, ?)";
	private static final String addIntoEmpRole = "INSERT INTO EmpRole (empId, empRole) VALUES (?,?)";

	private static final String userUpdateQuery = "update empData set empDOB = ?, empAddress = ?, empEmail = ?, updated_at = CURRENT_TIMESTAMP where empId = ?";
	private static final String adminUpdateQuery = "update empData set empName = ?, empDept = ?, empDOB = ?, empAddress = ?, empEmail = ?, updated_at = CURRENT_TIMESTAMP where empId = ?";

	private static final String deleteQuery = "update empData set isActive = false, deleted_at = CURRENT_TIMESTAMP where empId = ?";

	private static final String viewEmpDataQuery = "select * from empData  where isActive = true ORDER BY CAST(substring(empId from 4) AS INTEGER)";
	private static final String viewEmpDataQueryById = "select * from empData where empId = ? and isActive = true";
	private static final String fetchInactiveEmpQuery = "select * from empData  where isActive = false ORDER BY CAST(substring(empId from 4) AS INTEGER)";

	private static final String changePassQuery = "update empLogin set empPass = ?, password_changed_at = CURRENT_TIMESTAMP where empId = ?";
	private static final String resetPassQuery = "update empLogin set empPass = ?, password_changed_at = CURRENT_TIMESTAMP where empId = ?";

	private static final String grantRoleQuery = "insert into empRole (empId, empRole) values (?, ?::RoleEnum) ON CONFLICT DO NOTHING;";
	private static final String revokeRoleQuery = "delete from empRole where empId = ? and empRole = ?::RoleEnum";

	private static final String loginQuery = "select empPass from empLogin join empData on empLogin.empId = empData.empId where empLogin.empId = ? and empData.isActive = true";
	private static final String roleQuery = "select empRole from EmpRole where empId = ?";

	EmployeeUtil util = new EmployeeUtil();

	private Connection startConnection() {
		Connection conn = util.startConnection();
		if (conn == null) {
			throw new DataAccessException("Failed database connection");
		}
		return conn;
	}

	private EmpLoginResult fail() {
		return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
	}

	private java.sql.Date toSqlDate(String dob) {
		try {
			java.util.Date javaDate = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
			return new java.sql.Date(javaDate.getTime());
		} catch (Exception e) {
			throw new DataAccessException("Error in converting java date to sql date");
		}
	}

	private Employee mapToEmployee(ResultSet rs) throws SQLException {
		String id = rs.getString("empId");
		String name = rs.getString("empName");
		String dept = rs.getString("empDept");
		String DOB = rs.getDate("empDOB").toString();
		String address = rs.getString("empAddress");
		String email = rs.getString("empEmail");

		Employee emp = new Employee(id, name, dept, DOB, address, email);
		return emp;
	}

	private void checkEmpExists(Connection conn, String checkId) {
		try (PreparedStatement checkEmpPstmt = conn.prepareStatement(checkEmpQuery)) {
			checkEmpPstmt.setString(1, checkId);
			try (ResultSet rs = checkEmpPstmt.executeQuery()) {
				if (!rs.next()) {
					throw new EmployeeDoesNotExistException("Employee doesn't exist");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error checking emp in db" + e.getMessage());
		}
	}

	private List<EMSRoles> fetchRoles(Connection conn, String id) throws SQLException {
		try (PreparedStatement fetchRolePstmt = conn.prepareStatement(roleQuery);) {
			List<EMSRoles> empRolesList = new ArrayList<>();
			fetchRolePstmt.setString(1, id);
			try (ResultSet rs1 = fetchRolePstmt.executeQuery()) {
				while (rs1.next()) {
					empRolesList.add(EMSRoles.valueOf(rs1.getString("empRole")));
				}
			}
			return empRolesList;
		}
	}

	public void addEmployee(String name, String dept, String DOB, String address, String email,
			List<EMSRoles> rolesArray, String hashPassword) {
		Connection conn = null;
		try {
			conn = startConnection();
			conn.setAutoCommit(false);
			try (PreparedStatement addToEmpDataPstmt = conn.prepareStatement(addIntoEmpData, new String[] { "empid" });
					PreparedStatement addToEmpLogPstmt = conn.prepareStatement(addIntoEmpLogin);
					PreparedStatement addToEmpRolePstmt = conn.prepareStatement(addIntoEmpRole);) {

				addToEmpDataPstmt.setString(1, name);
				addToEmpDataPstmt.setString(2, dept);
				addToEmpDataPstmt.setDate(3, toSqlDate(DOB));
				addToEmpDataPstmt.setString(4, address);
				addToEmpDataPstmt.setString(5, email);
				addToEmpDataPstmt.executeUpdate();

				String generatedId = "";
				try (ResultSet rs = addToEmpDataPstmt.getGeneratedKeys()) {
					if (!rs.next()) {
						throw new SQLException("Failed to get generated employee ID");
					}
					generatedId = rs.getString(1);
				}

				addToEmpLogPstmt.setString(1, generatedId);
				addToEmpLogPstmt.setString(2, hashPassword);
				addToEmpLogPstmt.executeUpdate();

				for (EMSRoles role : rolesArray) {
					addToEmpRolePstmt.setString(1, generatedId);
					addToEmpRolePstmt.setObject(2, role.name(), java.sql.Types.OTHER);
					addToEmpRolePstmt.addBatch();
				}
				addToEmpRolePstmt.executeBatch();
				conn.commit();
			}
		} catch (SQLException e) {

			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					throw new DataAccessException("Rollback failed");
				}
			}
			throw new DataAccessException("Error inserting employee");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public void updateEmployee(Employee employee, EMSRoles role) {
		try (Connection conn = startConnection();) {
			int row;
			if (role == EMSRoles.USER) {
				try (PreparedStatement userPstmt = conn.prepareStatement(userUpdateQuery);) {
					userPstmt.setDate(1, toSqlDate(employee.getDOB()));
					userPstmt.setString(2, employee.getAddress());
					userPstmt.setString(3, employee.getEmail());
					userPstmt.setString(4, employee.getId());
					row = userPstmt.executeUpdate();
				}
			} else {
				try (PreparedStatement adminPstmt = conn.prepareStatement(adminUpdateQuery);) {
					adminPstmt.setString(1, employee.getName());
					adminPstmt.setString(2, employee.getDept());
					adminPstmt.setDate(3, toSqlDate(employee.getDOB()));
					adminPstmt.setString(4, employee.getAddress());
					adminPstmt.setString(5, employee.getEmail());
					adminPstmt.setString(6, employee.getId());
					row = adminPstmt.executeUpdate();
				}
			}
			if (row == 0) {
				throw new EmployeeDoesNotExistException("Employee doesn't exist");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error while updating employee" + e.getMessage());
		}
	}

	@Override
	public void deleteEmployee(String id) {

		try (Connection conn = startConnection(); PreparedStatement delPstmt = conn.prepareStatement(deleteQuery);) {
			delPstmt.setString(1, id);
			int row = delPstmt.executeUpdate();
			if (row == 0) {
				throw new EmployeeDoesNotExistException("Employee doesn't exist");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error while deleting employee" + e.getMessage());
		}
	}

	@Override
	public List<Employee> viewAllEmployees() {
		List<Employee> empList = new ArrayList<>();
		try (Connection conn = startConnection();
				PreparedStatement viewAllStmt = conn.prepareStatement(viewEmpDataQuery);
				ResultSet rs = viewAllStmt.executeQuery()) {
			while (rs.next()) {
				empList.add(mapToEmployee(rs));
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error reading from database ");
		}
		return empList;
	}

	@Override
	public Employee viewEmployeeById(String id) {
		try (Connection conn = startConnection();
				PreparedStatement viewByIdPstmt = conn.prepareStatement(viewEmpDataQueryById);) {
			viewByIdPstmt.setString(1, id);
			try (ResultSet rs = viewByIdPstmt.executeQuery();) {
				if (rs.next()) {
					return mapToEmployee(rs);
				}
				throw new EmployeeDoesNotExistException("Employee " + id + "doesn't exist");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error reading from database ");
		}
	}

	@Override
	public void changePassword(String id, String password) {

		try (Connection conn = startConnection();
				PreparedStatement changePassPstmt = conn.prepareStatement(changePassQuery)) {
			changePassPstmt.setString(1, password);
			changePassPstmt.setString(2, id);
			int row = changePassPstmt.executeUpdate();
			if (row == 0) {
				throw new EmployeeDoesNotExistException("Employee doesn't exist");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error changing password ");

		}
	}

	@Override
	public String resetPassword(String id) {

		String defPassword = util.generateRandomPassword();
		String hashPassword = util.generateHash(defPassword);
		try (Connection conn = startConnection();
				PreparedStatement resetPassPstmt = conn.prepareStatement(resetPassQuery);) {
			resetPassPstmt.setString(1, hashPassword);
			resetPassPstmt.setString(2, id);
			int row = resetPassPstmt.executeUpdate();
			if (row == 0) {
				throw new EmployeeDoesNotExistException("Employee with ID " + id + " does not exist");
			}
			return defPassword;
		} catch (SQLException e) {
			throw new DataAccessException("Error while resetting password");
		}
	}

	@Override
	public void grantRole(String id, EMSRoles role) {
		try (Connection conn = startConnection();) {
			checkEmpExists(conn, id);
			try (PreparedStatement grantRolePstmt = conn.prepareStatement(grantRoleQuery)) {
				grantRolePstmt.setString(1, id);
				grantRolePstmt.setObject(2, role.name(), java.sql.Types.OTHER);
				int row = grantRolePstmt.executeUpdate();
				if (row == 0) {
					throw new ValidationException("Employee Role already exists");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error while granting role " + e.getMessage());
		}
	}

	@Override
	public void revokeRole(String id, EMSRoles role) {
		try (Connection conn = startConnection();) {
			checkEmpExists(conn, id);
			try (PreparedStatement revokeRolePstmt = conn.prepareStatement(revokeRoleQuery)) {
				revokeRolePstmt.setString(1, id);
				revokeRolePstmt.setObject(2, role.name(), java.sql.Types.OTHER);
				int row = revokeRolePstmt.executeUpdate();
				if (row == 0) {
					throw new ValidationException("Employee Role doesn't exists");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error while revoking role " + e.getMessage());
		}
	}

	public EmpLoginResult validateLogin(String id, String password) {
		try (Connection conn = startConnection(); PreparedStatement loginPstmt = conn.prepareStatement(loginQuery)) {
			loginPstmt.setString(1, id);
			try (ResultSet rs = loginPstmt.executeQuery()) {
				if (!rs.next()) {
					return fail();
				}
				if (!rs.getString("empPass").equals(password)) {
					return fail();
				}
				List<EMSRoles> empRolesList = fetchRoles(conn, id);
				return new EmpLoginResult(EMSLoginResult.SUCCESS, id, empRolesList);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	public List<Employee> fetchInactiveEmployees() {
		List<Employee> empList = new ArrayList<>();
		try (Connection conn = startConnection();
				PreparedStatement viewAllStmt = conn.prepareStatement(fetchInactiveEmpQuery);
				ResultSet rs = viewAllStmt.executeQuery()) {
			while (rs.next()) {
				empList.add(mapToEmployee(rs));
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error reading from database ");
		}
		return empList;
	}
}
