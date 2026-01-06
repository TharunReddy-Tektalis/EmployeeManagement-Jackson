package com.employee.services;

import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class ChangeRole {
	EmployeeDAO dao = new EmployeeDAOImpl();
	ServerSideValidations validations = new ServerSideValidations();
	JSONParser parser = new JSONParser();
	EmployeeUtil util = new EmployeeUtil();
	GetEmployee getEmployee = new GetEmployee();
	Scanner sc = new Scanner(System.in);
	
	public void grantRole() {	
		System.out.print("Enter emp id to grant role:");
		String id = sc.next();
		if(!util.validateID(id)) return;
		boolean present = validations.checkEmployee(id);
		
		if(present) {
			System.out.print("Enter new role:");
			String role = sc.next();
			sc.nextLine();
			if(!util.validateRole(role)) return;

			dao.grantRole(id, role);
		}
		else {
			System.out.println("Employee doesn't exist");
		}
	}

	public void revokeRole() {

		System.out.print("Enter emp id to revoke role:");
		String id = sc.next();
		if(!util.validateID(id)) return;
		boolean present = validations.checkEmployee(id);
		if(present) {
			System.out.print("Enter role to revoke:");
			String role = sc.next();
			sc.nextLine();
			if(!util.validateRole(role)) return;
			
			dao.revokeRole(id, role);
		}
		else {
			System.out.println("Employee doesn't exist");
		}
	}
}
