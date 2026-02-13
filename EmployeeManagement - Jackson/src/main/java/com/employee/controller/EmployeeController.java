package com.employee.controller;

import java.util.List;
import java.util.Scanner;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSRoles;
import com.employee.exception.InputInvalidException;
import com.employee.exception.ServiceException;
import com.employee.model.Employee;
import com.employee.model.UserContext;
import com.employee.services.EmployeeServices;

public class EmployeeController {
	EmployeeServices empServices = new EmployeeServices();
	Scanner sc = new Scanner(System.in);

	public void addEmployee(EmployeeDAO dao) {
		System.out.print("Enter emp first name:");
		String fname = sc.nextLine();

		System.out.print("Enter emp last name:");
		String lname = sc.nextLine();
		String name = fname + " " + lname;

		System.out.print("Enter emp dept:");
		String dept = sc.nextLine();

		System.out.println("Enter DOB (dd-mm-yyyy)");
		String DOB = sc.nextLine();

		System.out.print("Enter emp address:");
		String address = sc.nextLine();

		System.out.print("Enter emp email:");
		String email = sc.nextLine();

		System.out.print("Enter emp role:");
		String role = sc.nextLine();

		Employee employee = new Employee(name, dept, DOB, address, email);
		try {
			String tempPassword = empServices.addEmployee(dao, employee, role);
			System.out.println("Employee added successfully");
			System.out.println("Your temporary password " + tempPassword);
		} catch (InputInvalidException e) {
			System.out.println("Error while inserting employee: " + e.getMessage());
		}
	}

	public void viewAllEmp(EmployeeDAO dao) {
		try {
			List<Employee> empList = empServices.viewAllEmp(dao);
			for (Employee employee : empList) {
				System.out.println(employee.toString());
			}
		} catch (ServiceException e) {
			System.out.println("Error while retrieving employees: " + e.getMessage());
		}
	}

	public void viewEmpByID(EmployeeDAO dao, UserContext userContext) {
		if (userContext.getEmpRoles().size() == 1 && userContext.getEmpRoles().contains(EMSRoles.USER)) {
			try {
				Employee employee = empServices.viewEmpByID(dao, userContext, userContext.getEmpId());
				System.out.println(employee.toString());
			} catch (ServiceException e) {
				System.out.println("error while fetching details:" + e.getMessage());
			}
		} else {
			System.out.println("Enter Emp ID: ");
			String id = sc.nextLine();
			try {
				Employee employee = empServices.viewEmpByID(dao, userContext, id);
				System.out.println(employee.toString());
			} catch (ServiceException e) {
				System.out.println("error while fetching details:" + e.getMessage());
			}
		}
	}

	public void deleteEmp(EmployeeDAO dao) {
		System.out.println("Enter ID to delete: ");
		String id = sc.nextLine();
		try {
			empServices.deleteEmp(dao, id);
			System.out.println("Employee deleted successfully");
		} catch (ServiceException e) {
			System.out.println("error while deleting employee:" + e.getMessage());
		}
	}

	public void updateEmp(EmployeeDAO dao, UserContext userContext) {
		String id;
		if (userContext.isAdminOrManager()) {
			System.out.print("Enter emp id:");
			id = sc.nextLine();
		} else {
			id = userContext.getEmpId();
		}

		String dept = "";
		String name = "";
		if (userContext.isAdminOrManager()) {
			System.out.print("Enter emp first name:");
			String fname = sc.nextLine();

			System.out.print("Enter emp last name:");
			String lname = sc.nextLine();
			name = fname + " " + lname;

			System.out.print("Enter emp dept:");
			dept = sc.nextLine();
		}

		System.out.println("Enter DOB (dd-mm-yyyy)");
		String DOB = sc.nextLine();

		System.out.print("Enter emp address:");
		String address = sc.nextLine();

		System.out.print("Enter emp email:");
		String email = sc.nextLine();

		Employee employee = new Employee(id, name, dept, DOB, address, email);
		try {
			empServices.updateEmp(dao, employee, userContext);
			System.out.println("Employee updated successfully");
		} catch (InputInvalidException | ServiceException e) {
			System.out.println("Error while updating employee: " + e.getMessage());
		}
	}

	public void grantEmpRole(EmployeeDAO dao) {
		System.out.print("Enter emp id to grant role:");
		String id = sc.nextLine();

		System.out.print("Enter new role:");
		String role = sc.nextLine();
		try {
			empServices.grantEmpRole(id, role, dao);
			System.out.println("Role granted successfully");
		} catch (ServiceException | InputInvalidException e) {
			System.out.println("Error while Grant Role: " + e.getMessage());
		}
	}

	public void revokeEmpRole(EmployeeDAO dao) {
		System.out.print("Enter emp id to revoke role:");
		String id = sc.nextLine();

		System.out.print("Enter role:");
		String role = sc.nextLine();
		try {
			empServices.revokeEmpRole(id, role, dao);
			System.out.println("Role revoked successfully");
		} catch (ServiceException | InputInvalidException e) {
			System.out.println("Error while Revoke Role: " + e.getMessage());
		}
	}
	
	public void fetchInactiveEmployees(EmployeeDAO dao) {
		try {
			List<Employee> empList = empServices.fetchInactiveEmployees(dao);
			for (Employee employee : empList) {
				System.out.println(employee.toString());
			}
		} catch (ServiceException e) {
			System.out.println("Error while retrieving employees: " + e.getMessage());
		}
	}
}
