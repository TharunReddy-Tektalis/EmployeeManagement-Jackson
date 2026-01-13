package com.employee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.employee.enums.EMSLoginResult;
import com.employee.enums.EMSRoles;
import com.employee.model.EmpLoginResult;
import com.employee.util.EmployeeUtil;

public class EmployeeDbDAOImpl implements EmployeeDAO {

	EmployeeUtil util = new EmployeeUtil();
	public Connection conn = util.startConnection();

	private boolean checkEmpExists(String checkId) {
		String checkEmpQuery = "select empId from empData where empId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(checkEmpQuery);
			pstmt.setString(1, checkId);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Employee doesn't exists");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return true;
	}

	private boolean checkRoleExists(String id, String role) {
		String checkRoleQuery = "select empRole from EmpRole where empId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(checkRoleQuery);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(role)) {
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error checking emp in db" + e.getMessage());
		}
		return false;
	}

	public void addEmployee(String name, String dept, String DOB, String address, String email,
			List<EMSRoles> rolesArray, String hashPassword) {
		try {
			conn.setAutoCommit(false);
			String query1 = "INSERT INTO EmpData (empName, empDept, empDOB, empAddress, empEmail) VALUES (?,?, ?, ?, ?)";

			PreparedStatement pstmt = conn.prepareStatement(query1, new String[] { "empid" });
			pstmt.setString(1, name);
			pstmt.setString(2, dept);

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date javaDate = sdf.parse(DOB);

			java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
			pstmt.setDate(3, sqlDate);

			pstmt.setString(4, address);
			pstmt.setString(5, email);
			pstmt.executeUpdate();

			String generatedId = "";
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				generatedId = rs.getString(1);
			}

			String query2 = "INSERT INTO EmpLogin (empId, empPass) VALUES (?, ?)";
			PreparedStatement pstmt1 = conn.prepareStatement(query2);
			pstmt1.setString(1, generatedId);
			pstmt1.setString(2, hashPassword);
			pstmt1.executeUpdate();

			String query3 = "INSERT INTO EmpRole (empId, empRole) VALUES (?,?)";
			PreparedStatement pstmt2 = conn.prepareStatement(query3);
			for (EMSRoles role : rolesArray) {
				pstmt2.setString(1, generatedId);
				pstmt2.setObject(2, role.name(), java.sql.Types.OTHER);
				pstmt2.executeUpdate();
			}
			
			conn.commit();
		} catch (ParseException e) {
			System.out.println("Error in formatting date" + e.getMessage());
		} catch (SQLException e) {
			if (conn != null) {
		        try {
		            System.err.println("transaction has rollback : " + e.getMessage());
		            conn.rollback();
		        } catch (SQLException ex) {
		            System.err.println("Error during rollback: " + ex.getMessage());
		        }
		    }
		}

	}

	@Override
	public void updateEmployee(String id, String name, String dept, String DOB, String address, String email,
			EMSRoles role) {
		
		if (checkEmpExists(id)) {
			String userUpdateQuery = "update empData set empDOB = ?, empAddress = ?, empEmail = ? where empId = ?";
			String adminUpdateQuery = "update empData set empName = ?, empDept = ?, empDOB = ?, empAddress = ?, empEmail = ? where empId = ?";
			if (role.equals(EMSRoles.USER)) {
				
				try {
					conn.setAutoCommit(false);
					PreparedStatement userPstmt = conn.prepareStatement(userUpdateQuery);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					java.util.Date javaDate = sdf.parse(DOB);

					java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
					userPstmt.setDate(1, sqlDate);
					userPstmt.setString(2, address);
					userPstmt.setString(3, email);
					userPstmt.setString(4, id);
					int row = userPstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Updated Successfully");
					}
					conn.commit();
				} catch (SQLException e) {
					System.out.println("Error while updating " + e.getMessage());
					if (conn != null) {
				        try {
				            System.err.println("transaction has rollback : " + e.getMessage());
				            conn.rollback();
				        } catch (SQLException ex) {
				            System.err.println("Error during rollback: " + ex.getMessage());
				        }
				    }
				} catch (ParseException e) {
					System.out.println("Error in formatting date" + e.getMessage());
				}
			} else {
				try {
					conn.setAutoCommit(false);
					PreparedStatement adminPstmt = conn.prepareStatement(adminUpdateQuery);

					adminPstmt.setString(1, name);
					adminPstmt.setString(2, dept);

					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					java.util.Date javaDate = sdf.parse(DOB);

					java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
					adminPstmt.setDate(3, sqlDate);

					adminPstmt.setString(4, address);
					adminPstmt.setString(5, email);
					adminPstmt.setString(6, id);
					int row = adminPstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Updated Successfully");
					}
					conn.commit();
				} catch (SQLException e) {
					System.out.println("Error while updating " + e.getMessage());
					if (conn != null) {
				        try {
				            System.err.println("transaction has rollback : " + e.getMessage());
				            conn.rollback();
				        } catch (SQLException ex) {
				            System.err.println("Error during rollback: " + ex.getMessage());
				        }
				    }
				} catch (ParseException e) {
					System.out.println("Error in formatting date" + e.getMessage());
				}
			}
		}
	}

	@Override
	public void deleteEmployee(String id) {
		if (checkEmpExists(id)) {
			try {
				conn.setAutoCommit(false);
				String deleteQuery = "delete from empData where empId = ?";
				PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
				pstmt.setString(1, id);
				int row = pstmt.executeUpdate();
				if (row != 0) {
					System.out.println("Deleted employee successfully");
				} else {
					System.out.println("Cannot delete emplpoyee");
				}
				conn.commit();
			} catch (SQLException e) {
				System.out.println("Error deleting from db" + e.getMessage());
				if (conn != null) {
			        try {
			            System.err.println("transaction has rollback : " + e.getMessage());
			            conn.rollback();
			        } catch (SQLException ex) {
			            System.err.println("Error during rollback: " + ex.getMessage());
			        }
			    }
			}
		}
	}

	@Override
	public void view_all_Employees() {
		try {
			String viewEmpDataQuery = "select * from empData";
			String viewEmpRolesQuery = "select empRole from empRole where empId = ?";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(viewEmpDataQuery);
			PreparedStatement pstmt = conn.prepareStatement(viewEmpRolesQuery);
			while (rs.next()) {
				String currId = rs.getString("empId");
				pstmt.setString(1, currId);
				ResultSet crs = pstmt.executeQuery();
				System.out.print("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("empName")
						+ " | Department: " + rs.getString("empDept") + " | DOB: " + rs.getDate("empDOB")
						+ " | Address: " + rs.getString("empAddress") + " | Email: " + rs.getString("empEmail")
						+ " | Roles: [ ");
				while (crs.next()) {
					System.out.print(crs.getString("empRole") + " ");

				}
				System.out.println("]");
				System.out.println();
			}
//			while (rs.next()) {
//				System.out.println();
//				System.out.println("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("empName")
//						+ " | Department: " + rs.getString("empDept") + " | DOB: " + rs.getDate("empDOB")
//						+ " | Address: " + rs.getString("empAddress") + " | Email: " + rs.getString("empEmail")
//						+ " | Roles: " + rs.getString("empRole"));
//			}
		} catch (SQLException e) {
			System.out.println("Error reading from database " + e.getMessage());
		}
	}

	@Override
	public void viewEmployee_by_id(String id) {
		if (checkEmpExists(id)) {
			try {
				String viewEmpDataQuery = "select * from empData where empId = ?";
				String viewEmpRolesQuery = "select empRole from empRole where empId = ?";
				PreparedStatement stmt = conn.prepareStatement(viewEmpDataQuery);
				stmt.setString(1, id);
				ResultSet rs = stmt.executeQuery();
				PreparedStatement pstmt = conn.prepareStatement(viewEmpRolesQuery);
				while (rs.next()) {
					String currId = rs.getString("empId");
					pstmt.setString(1, currId);
					ResultSet crs = pstmt.executeQuery();
					System.out.print("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("empName")
							+ " | Department: " + rs.getString("empDept") + " | DOB: " + rs.getDate("empDOB")
							+ " | Address: " + rs.getString("empAddress") + " | Email: " + rs.getString("empEmail")
							+ " | Roles: [ ");
					while (crs.next()) {
						System.out.print(crs.getString("empRole") + " ");

					}
					System.out.println("]");
					System.out.println();
				}
//				while (rs.next()) {
//					System.out.println();
//					System.out.println("Emp ID: " + rs.getString("empId") + " | Name: " + rs.getString("empName")
//							+ " | Department: " + rs.getString("empDept") + " | DOB: " + rs.getDate("empDOB")
//							+ " | Address: " + rs.getString("empAddress") + " | Email: " + rs.getString("empEmail")
//							+ " | Roles: " + rs.getString("empRole"));
//				}
			} catch (SQLException e) {
				System.out.println("Error reading from database " + e.getMessage());
			}
		}
	}

	@Override
	public void changePassword(String id, String password) {
		String changePassQuery = "update empLogin set empPass = ? where empId = ?";
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(changePassQuery);
			pstmt.setString(1, password);
			pstmt.setString(2, id);
			int row = pstmt.executeUpdate();
			if (row != 0) {
				System.out.println("Successfully changed password");
			} else {
				System.out.println("Cannot change emplpoyee password");
			}
		} catch (SQLException e) {
			System.out.println("Error changing password " + e.getMessage());
			if (conn != null) {
		        try {
		            System.err.println("transaction has rollback : " + e.getMessage());
		            conn.rollback();
		        } catch (SQLException ex) {
		            System.err.println("Error during rollback: " + ex.getMessage());
		        }
		    }
		}
	}

	@Override
	public void resetPassword(String id, String password) {
		String resetPassQuery = "update empLogin set empPass = ? where empId = ?";
		if (checkEmpExists(id)) {
			try {
				conn.setAutoCommit(false);
				PreparedStatement pstmt = conn.prepareStatement(resetPassQuery);
				pstmt.setString(1, password);
				pstmt.setString(2, id);
				int row = pstmt.executeUpdate();
				if (row != 0) {
					System.out.println("Successfully reset password");
				} else {
					System.out.println("Cannot reset emplpoyee password");
				}
			} catch (SQLException e) {
				System.out.println("Error reseting password " + e.getMessage());
				if (conn != null) {
			        try {
			            System.err.println("transaction has rollback : " + e.getMessage());
			            conn.rollback();
			        } catch (SQLException ex) {
			            System.err.println("Error during rollback: " + ex.getMessage());
			        }
			    }
			}
		}
	}

	@Override
	public void grantRole(String id, String role) {
		String grantRoleQuery = "insert into empRole (empId, empRole) values (?,?)";
		if (checkEmpExists(id)) {
			if (!checkRoleExists(id, role)) {
				try {
					conn.setAutoCommit(false);
					PreparedStatement pstmt = conn.prepareStatement(grantRoleQuery);
					pstmt.setString(1, id);
					pstmt.setObject(2, role, java.sql.Types.OTHER);
					int row = pstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Successfully Granted Role");
					} else {
						System.out.println("cannot Grant Role");
					}
				} catch (SQLException e) {
					System.out.println("Error in granting role " + e.getMessage());
					if (conn != null) {
				        try {
				            System.err.println("transaction has rollback : " + e.getMessage());
				            conn.rollback();
				        } catch (SQLException ex) {
				            System.err.println("Error during rollback: " + ex.getMessage());
				        }
				    }
				}
			} else {
				System.out.println("Role already exists");
			}
		} else {
			System.out.println("Employee doesn't exists");
		}
	}

	@Override
	public void revokeRole(String id, String role) {
		String revokeRoleQuery = "delete from empRole where empId = ? and empRole = ?";
		if (checkEmpExists(id)) {
			if (checkRoleExists(id, role)) {
				try {
					conn.setAutoCommit(false);
					PreparedStatement pstmt = conn.prepareStatement(revokeRoleQuery);
					pstmt.setString(1, id);
					pstmt.setObject(2, role, java.sql.Types.OTHER);
					int row = pstmt.executeUpdate();
					if (row != 0) {
						System.out.println("Successfully Revoked Role");
					} else {
						System.out.println("cannot revoke Role");
					}
					conn.commit();
				} catch (SQLException e) {
					System.out.println("Error in revoking role " + e.getMessage());
					if (conn != null) {
				        try {
				            System.err.println("transaction has rollback : " + e.getMessage());
				            conn.rollback();
				        } catch (SQLException ex) {
				            System.err.println("Error during rollback: " + ex.getMessage());
				        }
				    }
				}
			} else {
				System.out.println("Role doesn't exists");
			}
		} else {
			System.out.println("Employee doesn't exists");
		}

	}

	public EmpLoginResult validateLogin(String id, String password) {
		String loginQuery = "select empPass from empLogin where empId = ?";
		String roleQuery = "select empRole from EmpRole where empId = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(loginQuery);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Invalid Id");
				return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
			}
			if (rs.getString("empPass").equals(password)) {
				PreparedStatement pstmt1 = conn.prepareStatement(roleQuery);
				pstmt1.setString(1, id);
				ResultSet rs1 = pstmt1.executeQuery();
				List<EMSRoles> empRolesList = new ArrayList<>();
				while (rs1.next()) {
					empRolesList.add(EMSRoles.valueOf(rs1.getString("empRole")));
				}
				System.out.println("Login Successful");
				return new EmpLoginResult(EMSLoginResult.SUCCESS, id, empRolesList);
			}
		} catch (SQLException e) {
			System.out.println("Database error near login" + e.getMessage());
		}
		return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
	}
}
