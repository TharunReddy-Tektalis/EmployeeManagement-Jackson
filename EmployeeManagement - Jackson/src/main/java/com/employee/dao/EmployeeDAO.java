package com.employee.dao;

import java.util.List;

import com.employee.enums.EMSRoles;
import com.employee.model.EmpLoginResult;
import com.employee.model.Employee;

public interface EmployeeDAO {
	void addEmployee(String name, String dept, String DOB, String address, String email, List<EMSRoles> rolesArray, String hashPassword);
	void updateEmployee(Employee employee, EMSRoles role);
	void deleteEmployee(String id);
	List<Employee> viewAllEmployees();
	Employee viewEmployeeById(String id);
	void changePassword(String id, String password);
	String resetPassword(String id);
	void grantRole(String id, EMSRoles role);
	void revokeRole(String id, EMSRoles role);
	EmpLoginResult validateLogin(String id, String password);
	List<Employee> fetchInactiveEmployees();
}
