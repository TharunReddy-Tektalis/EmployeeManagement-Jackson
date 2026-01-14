package com.employee.dao;

import java.util.List;

import com.employee.enums.EMSRoles;
import com.employee.model.EmpLoginResult;

public interface EmployeeDAO {
	void addEmployee(String name, String dept, String DOB, String address, String email, List<EMSRoles> rolesArray, String hashPassword);
	void updateEmployee(String id,String name, String dept, String DOB, String address, String email, EMSRoles role);
	void deleteEmployee(String id);
	void viewAllEmployees();
	void viewEmployeeById(String id);
	void changePassword(String id, String password);
	void resetPassword(String id, String password);
	void grantRole(String id, EMSRoles role);
	void revokeRole(String id, EMSRoles role);
	EmpLoginResult validateLogin(String id, String password);
}
