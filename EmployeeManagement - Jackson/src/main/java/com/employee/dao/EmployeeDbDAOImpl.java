package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.employee.enums.EMSLoginResult;
import com.employee.enums.EMSRoles;
import com.employee.model.EmpLoginResult;
import com.employee.util.EmployeeUtil;

public class EmployeeDbDAOImpl implements EmployeeDAO {

	private static final String checkEmpQuery = "select 1 from empData where empId = ?";
	private static final String checkRoleQuery = "select 1 from EmpRole where empId = ? and empRole = ?";

	private static final String addIntoEmpData = "INSERT INTO EmpData (empName, empDept, empDOB, empAddress, empEmail) VALUES (?,?, ?, ?, ?)";
	private static final String addIntoEmpLogin = "INSERT INTO EmpLogin (empId, empPass) VALUES (?, ?)";
	private static final String addIntoEmpRole = "INSERT INTO EmpRole (empId, empRole) VALUES (?,?)";

	private static final String userUpdateQuery = "update empData set empDOB = ?, empAddress = ?, empEmail = ? where empId = ?";
	private static final String adminUpdateQuery = "update empData set empName = ?, empDept = ?, empDOB = ?, empAddress = ?, empEmail = ? where empId = ?";

	private static final String deleteQuery = "delete from empData where empId = ?";

	private static final String viewEmpDataQuery = "select * from empData ORDER BY CAST(substring(empId from 4) AS INTEGER)";
	private static final String viewEmpDataQueryById = "select * from empData where empId = ?";

	private static final String changePassQuery = "update empLogin set empPass = ? where empId = ?";
	private static final String resetPassQuery = "update empLogin set empPass = ? where empId = ?";

	private static final String grantRoleQuery = "insert into empRole (empId, empRole) values (?,?)";
	private static final String revokeRoleQuery = "delete from empRole where empId = ? and empRole = ?";

	private static final String loginQuery = "select empPass from empLogin where empId = ?";
	private static final String roleQuery = "select empRole from EmpRole where empId = ?";

	EmployeeUtil util = new EmployeeUtil();

	private Connection startConnection() {
		try {
			Connection conn = util.startConnection();
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException e) {
			System.out.println("Error in starting connection " + e.getMessage());
		}
		return null;
	}

	private void commitTransaction(Connection conn) {
		try {
			if (conn != null) {
				conn.commit();
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Error during commit " + e.getMessage());
		}
	}

	private void rollbackTransaction(Connection conn) {
		try {
			if (conn != null) {
				conn.rollback();
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Error during rollback " + e.getMessage());
		}
	}

	private java.sql.Date toSqlDate(String dob) {
		try {
			return java.sql.Date.valueOf(LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		} catch (Exception e) {
			System.out.println("Error in formatting date " + e.getMessage());
		}
		return null;
	}

	private void printEmployee(ResultSet rs) throws SQLException {
		System.out.println();
		System.out.println("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("empName")
				+ " | Department: " + rs.getString("empDept") + " | DOB: " + rs.getDate("empDOB") + " | Address: "
				+ rs.getString("empAddress") + " | Email: " + rs.getString("empEmail"));
	}

	private boolean checkEmpExists(Connection conn, String checkId) {

		try (PreparedStatement pstmt = conn.prepareStatement(checkEmpQuery)) {
			pstmt.setString(1, checkId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return false;
	}

	private boolean checkRoleExists(Connection conn, String id, EMSRoles role) {

		try (PreparedStatement pstmt = conn.prepareStatement(checkRoleQuery)) {
			pstmt.setString(1, id);
			pstmt.setString(2, role.name());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return false;
	}

	private List<EMSRoles> fetchRoles(Connection conn, String id) throws SQLException {
		try (PreparedStatement pstmt1 = conn.prepareStatement(roleQuery);) {
			List<EMSRoles> empRolesList = new ArrayList<>();
			pstmt1.setString(1, id);
			try (ResultSet rs1 = pstmt1.executeQuery()) {
				while (rs1.next()) {
					empRolesList.add(EMSRoles.valueOf(rs1.getString("empRole")));
				}
			}
			return empRolesList;
		}
	}

	public void addEmployee(String name, String dept, String DOB, String address, String email,
			List<EMSRoles> rolesArray, String hashPassword) {
		Connection conn = startConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(addIntoEmpData, new String[] { "empid" });
				PreparedStatement pstmt1 = conn.prepareStatement(addIntoEmpLogin);
				PreparedStatement pstmt2 = conn.prepareStatement(addIntoEmpRole);) {
			pstmt.setString(1, name);
			pstmt.setString(2, dept);
			pstmt.setDate(3, toSqlDate(DOB));
			pstmt.setString(4, address);
			pstmt.setString(5, email);
			pstmt.executeUpdate();
			String generatedId = "";
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (!rs.next()) {
					throw new SQLException("Failed to get generated employee ID");
				}
				generatedId = rs.getString(1);
			}
			pstmt1.setString(1, generatedId);
			pstmt1.setString(2, hashPassword);
			pstmt1.executeUpdate();
			for (EMSRoles role : rolesArray) {
				pstmt2.setString(1, generatedId);
				pstmt2.setObject(2, role.name(), java.sql.Types.OTHER);
				pstmt2.executeUpdate();
			}
			commitTransaction(conn);
			System.out.println("Inserted Employee successfully");
		} catch (SQLException e) {
			rollbackTransaction(conn);
			System.out.println("Error inserting employee: " + e.getMessage());
		}
	}

	@Override
	public void updateEmployee(String id, String name, String dept, String DOB, String address, String email,
			EMSRoles role) {
		Connection conn = startConnection();
		if (checkEmpExists(conn, id)) {
			if (role.equals(EMSRoles.USER)) {
				try (PreparedStatement userPstmt = conn.prepareStatement(userUpdateQuery);) {
					userPstmt.setDate(1, toSqlDate(DOB));
					userPstmt.setString(2, address);
					userPstmt.setString(3, email);
					userPstmt.setString(4, id);
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
					adminPstmt.setString(1, name);
					adminPstmt.setString(2, dept);
					adminPstmt.setDate(3, toSqlDate(DOB));
					adminPstmt.setString(4, address);
					adminPstmt.setString(5, email);
					adminPstmt.setString(6, id);
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
		}
	}

	@Override
	public void deleteEmployee(String id) {
		Connection conn = startConnection();
		if (checkEmpExists(conn, id)) {
			try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery);) {
				pstmt.setString(1, id);
				int row = pstmt.executeUpdate();
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
		}
	}

	@Override
	public void viewAllEmployees() {
		try (Connection conn = startConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(viewEmpDataQuery)) {
			while (rs.next()) {
				printEmployee(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
		}
	}

	@Override
	public void viewEmployeeById(String id) {
		try (Connection conn = startConnection();
				PreparedStatement stmt = conn.prepareStatement(viewEmpDataQueryById);
				ResultSet rs = stmt.executeQuery();) {
			if (checkEmpExists(conn, id)) {
				stmt.setString(1, id);
				while (rs.next()) {
					printEmployee(rs);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
		}
	}

	@Override
	public void changePassword(String id, String password) {
		Connection conn = startConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(changePassQuery)) {
			pstmt.setString(1, password);
			pstmt.setString(2, id);
			int row = pstmt.executeUpdate();
			if (row != 0) {
				System.out.println("Successfully changed password");
				commitTransaction(conn);
				return;
			}
			System.out.println("Cannot change emplpoyee password");
		} catch (SQLException e) {
			rollbackTransaction(conn);
			System.out.println("Error while changing password " + e.getMessage());
		}
	}

	@Override
	public void resetPassword(String id, String password) {
		Connection conn = startConnection();

		if (checkEmpExists(conn, id)) {
			try (PreparedStatement pstmt = conn.prepareStatement(resetPassQuery);) {
				pstmt.setString(1, password);
				pstmt.setString(2, id);
				int row = pstmt.executeUpdate();
				if (row != 0) {
					System.out.println("Successfully reset password");
					commitTransaction(conn);
					return;
				}
				System.out.println("Cannot reset emplpoyee password");
			} catch (SQLException e) {
				rollbackTransaction(conn);
				System.out.println("Error while reseting password " + e.getMessage());
			}
		}
	}

	@Override
	public void grantRole(String id, EMSRoles role) {
		Connection conn = startConnection();
		if (checkEmpExists(conn, id)) {
			if (!checkRoleExists(conn, id, role)) {
				try (PreparedStatement pstmt = conn.prepareStatement(grantRoleQuery)) {
					pstmt.setString(1, id);
					pstmt.setString(2, role.name());
					int row = pstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Successfully Granted Role");
						return;
					}
					System.out.println("cannot Grant Role");
					commitTransaction(conn);
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
				try (PreparedStatement pstmt = conn.prepareStatement(revokeRoleQuery)) {
					pstmt.setString(1, id);
					pstmt.setString(2, role.name());
					int row = pstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Successfully Revoked Role");
						return;
					}
					System.out.println("cannot revoke Role");
					commitTransaction(conn);
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
		try (Connection conn = startConnection(); PreparedStatement pstmt = conn.prepareStatement(loginQuery)) {
			pstmt.setString(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.next()) {
					System.out.println("Invalid Id");
					return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
				}
				if (!rs.getString("empPass").equals(password)) {
					System.out.println("Invalid Password");
					return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
				}
				List<EMSRoles> empRolesList = fetchRoles(conn, id);
				System.out.println("Login Successful");
				return new EmpLoginResult(EMSLoginResult.SUCCESS, id, empRolesList);
			}
		} catch (SQLException e) {
			System.out.println("Database error near login" + e.getMessage());
		}
		return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
	}
}
