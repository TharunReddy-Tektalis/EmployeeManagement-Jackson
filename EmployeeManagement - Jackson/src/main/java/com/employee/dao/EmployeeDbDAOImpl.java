package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.employee.enums.EMSLoginResult;
import com.employee.enums.EMSRoles;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.model.EmpLoginResult;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class EmployeeDbDAOImpl implements EmployeeDAO {

	private static final String checkEmpQuery = "select 1 from empData where empId = ?";
	private static final String checkRoleQuery = "select 1 from EmpRole where empId = ? and empRole = ?";

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

	private static final String grantRoleQuery = "insert into empRole (empId, empRole) values (?, ?::RoleEnum);";
	private static final String revokeRoleQuery = "delete from empRole where empId = ? and empRole = ?::RoleEnum;";

	private static final String loginQuery = "select empPass from empLogin where empId = ? ";
	private static final String roleQuery = "select empRole from EmpRole where empId = ?";

	EmployeeUtil util = new EmployeeUtil();

	private Connection startConnection() {
		try {
			Connection conn = util.startConnection();
			if (conn == null) {
				throw new DataAccessException("Failed database connection");
			}

			conn.setAutoCommit(false);
			return conn;

		} catch (SQLException e) {
			throw new DataAccessException("Error while starting connection");
		}
	}

	private void commitTransaction(Connection conn) {
		try {
			if (conn != null) {
				conn.commit();
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error during commit");
		}
	}

	private void rollbackTransaction(Connection conn) {
		try {
			if (conn != null) {
				conn.rollback();
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error during commit");
		}
	}

	private EmpLoginResult fail() {
		return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
	}

	private java.sql.Date toSqlDate(String dob) {
		try {
			java.util.Date javaDate = new SimpleDateFormat("dd-MM-yyyy").parse(dob);
			return new java.sql.Date(javaDate.getTime());
		} catch (Exception e) {
			System.out.println("Error in formatting date " + e.getMessage());
		}
		return null;
	}

	private Employee mapToEmployee(ResultSet rs) throws SQLException {
		String id = rs.getString("empId");
		String name = rs.getString("empName");
		String dept = rs.getString("empDept");
		String DOB = String.valueOf(rs.getDate("empDOB"));
		String address = rs.getString("empAddress");
		String email = rs.getString("empEmail");

		Employee emp = new Employee(id, name, dept, DOB, address, email);
		return emp;
	}

	private boolean checkEmpExists(Connection conn, String checkId) {

		try (PreparedStatement checkEmpPstmt = conn.prepareStatement(checkEmpQuery)) {
			checkEmpPstmt.setString(1, checkId);
			try (ResultSet rs = checkEmpPstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return false;
	}

	private boolean checkRoleExists(Connection conn, String id, EMSRoles role) {

		try (PreparedStatement checkRolePstmt = conn.prepareStatement(checkRoleQuery)) {
			checkRolePstmt.setString(1, id);
			checkRolePstmt.setObject(2, role.name(), java.sql.Types.OTHER);
			try (ResultSet rs = checkRolePstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return false;
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

//				commitTransaction(conn);
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
		Connection conn = startConnection();
		if (checkEmpExists(conn, employee.getId())) {
			if (role.equals(EMSRoles.USER)) {
				try (PreparedStatement userPstmt = conn.prepareStatement(userUpdateQuery);) {
					userPstmt.setDate(1, toSqlDate(employee.getDOB()));
					userPstmt.setString(2, employee.getAddress());
					userPstmt.setString(3, employee.getEmail());
					userPstmt.setString(4, employee.getId());
					int row = userPstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Updated Successfully");
					}
					commitTransaction(conn);
				} catch (SQLException e) {
					rollbackTransaction(conn);
					System.out.println("Error while updating " + e.getMessage());
				}
			} else {
				try (PreparedStatement adminPstmt = conn.prepareStatement(adminUpdateQuery);) {
					adminPstmt.setString(1, employee.getName());
					adminPstmt.setString(2, employee.getDept());
					adminPstmt.setDate(3, toSqlDate(employee.getDOB()));
					adminPstmt.setString(4, employee.getAddress());
					adminPstmt.setString(5, employee.getEmail());
					adminPstmt.setString(6, employee.getId());
					int row = adminPstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Updated Successfully");
					}
					commitTransaction(conn);
				} catch (SQLException e) {
					rollbackTransaction(conn);
					System.out.println("Error while updating " + e.getMessage());
				}
			}
		} else {
			throw new DataAccessException("Employee with ID " + employee.getId() + " does not exist");
		}
	}

	@Override
	public void deleteEmployee(String id) {
		Connection conn = startConnection();
		if (checkEmpExists(conn, id)) {
			try (PreparedStatement delPstmt = conn.prepareStatement(deleteQuery);) {
				delPstmt.setString(1, id);
				int row = delPstmt.executeUpdate();
				if (row != 0) {
					System.out.println("Deleted employee successfully");
				} else {
					System.out.println("Cannot delete emplpoyee");
				}
				commitTransaction(conn);
			} catch (SQLException e) {
				rollbackTransaction(conn);
				System.out.println("Error while updating " + e.getMessage());
			}
		} else {
			System.out.println("Employee Doesn't exist");
		}
	}

	@Override
	public List<Employee> viewAllEmployees() {
		List<Employee> empList = new ArrayList<>();
		try (Connection conn = startConnection();
				Statement viewAllStmt = conn.createStatement();
				ResultSet rs = viewAllStmt.executeQuery(viewEmpDataQuery)) {
			while (rs.next()) {
//				printEmployee(rs);
				Employee emp = mapToEmployee(rs);
				empList.add(emp);
			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
		}
		return empList;
	}

	@Override
	public Employee viewEmployeeById(String id) {
		try (Connection conn = startConnection();) {
			if (checkEmpExists(conn, id)) {
				try (PreparedStatement viewByIdPstmt = conn.prepareStatement(viewEmpDataQueryById);) {
					viewByIdPstmt.setString(1, id);
					try (ResultSet rs = viewByIdPstmt.executeQuery();) {
						while (rs.next()) {
//							printEmployee(rs);
							return mapToEmployee(rs);
						}
					}
				}
			} else {
				System.out.println("Employee Doesn't exist");
			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
		}
		return null;
	}

	@Override
	public void changePassword(String id, String password) {
		Connection conn = startConnection();
		try (PreparedStatement changePassPstmt = conn.prepareStatement(changePassQuery)) {
			changePassPstmt.setString(1, password);
			changePassPstmt.setString(2, id);
			int row = changePassPstmt.executeUpdate();
			if (row != 0) {
				commitTransaction(conn);
				return;
			}
		} catch (SQLException e) {
			rollbackTransaction(conn);
		}
	}

	@Override
	public String resetPassword(String id) {
		Connection conn = startConnection();
		String defPassword = util.generateRandomPassword();
		String hashPassword = util.generateHash(defPassword);
//		if (checkEmpExists(conn, id)) {
		try (PreparedStatement resetPassPstmt = conn.prepareStatement(resetPassQuery);) {
			resetPassPstmt.setString(1, hashPassword);
			resetPassPstmt.setString(2, id);
			int row = resetPassPstmt.executeUpdate();
			if (row == 0) {
				rollbackTransaction(conn);
				throw new EmployeeDoesNotExistException("Employee with ID " + id + " does not exist");
			}
			commitTransaction(conn);
			return defPassword;
		} catch (SQLException e) {
			rollbackTransaction(conn);
			throw new DataAccessException("Error while resetting password");
		}
	}

	@Override
	public void grantRole(String id, EMSRoles role) {
		Connection conn = startConnection();
		if (checkEmpExists(conn, id)) {
			if (!checkRoleExists(conn, id, role)) {
				try (PreparedStatement grantRolePstmt = conn.prepareStatement(grantRoleQuery)) {
					grantRolePstmt.setString(1, id);
					grantRolePstmt.setObject(2, role.name(), java.sql.Types.OTHER);
					int row = grantRolePstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Successfully Granted Role");
						commitTransaction(conn);
						return;
					}
					System.out.println("cannot Grant Role");
				} catch (SQLException e) {
					rollbackTransaction(conn);
					System.out.println("Error while reseting password " + e.getMessage());
				}
			} else {
				System.out.println("Role already exists");
			}
		} else {
			System.out.println("Employee doesn't exists");
		}
	}

	@Override
	public void revokeRole(String id, EMSRoles role) {
		Connection conn = startConnection();
		if (checkEmpExists(conn, id)) {
			if (checkRoleExists(conn, id, role)) {
				try (PreparedStatement revokeRolePstmt = conn.prepareStatement(revokeRoleQuery)) {
					revokeRolePstmt.setString(1, id);
					revokeRolePstmt.setObject(2, role.name(), java.sql.Types.OTHER);
					int row = revokeRolePstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Successfully Revoked Role");
						commitTransaction(conn);
						return;
					}
					System.out.println("cannot revoke Role");
				} catch (SQLException e) {
					rollbackTransaction(conn);
					System.out.println("Error while reseting password " + e.getMessage());
				}
			} else {
				System.out.println("Role doesn't exists");
			}
		} else {
			System.out.println("Employee doesn't exists");
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
			throw new DataAccessException("Error while validating login");
		}
	}

	public List<Employee> fetchInactiveEmployees() {
		List<Employee> empList = new ArrayList<>();
		try (Connection conn = startConnection();
				Statement viewAllStmt = conn.createStatement();
				ResultSet rs = viewAllStmt.executeQuery(fetchInactiveEmpQuery)) {
			while (rs.next()) {
//				printEmployee(rs);
				Employee emp = mapToEmployee(rs);
				empList.add(emp);
			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
		}
		return empList;
	}
}
