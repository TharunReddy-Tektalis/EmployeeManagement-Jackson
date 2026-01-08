package com.employee.dao;

import org.json.simple.JSONArray;

public interface EmployeeDAO {
	void addEmployee(String name, String dept, String DOB, String address, String email, JSONArray rolesArray, String hashPassword);
	void updateEmployee(String id,String name, String dept, String DOB, String address, String email);
	void deleteEmployee(String id);
	void view_all_Employees();
	void viewEmployee_by_id(String id);
	void changePassword(String id, String password);
	void resetPassword(String id, String password);
	void grantRole(String id, String role);
	void revokeRole(String id, String role);
}
