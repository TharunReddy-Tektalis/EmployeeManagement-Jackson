package com.employee.services;

import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeFileDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class ChangeEmpRole {
//	EmployeeDAO dao = new EmployeeFileDAOImpl();
	ServerSideValidations validations = new ServerSideValidations();
	JSONParser parser = new JSONParser();
	EmployeeUtil util = new EmployeeUtil();
	ViewEmpDetails getEmployee = new ViewEmpDetails();
	Scanner sc = new Scanner(System.in);

	public void grantEmpRole(EmployeeDAO dao) {
		System.out.print("Enter emp id to grant role:");
		String id = sc.next();
		if (!util.validateID(id))
			return;

//		if (validations.checkEmpExists(id)) {
			System.out.print("Enter new role:");
			String role = sc.next();
			sc.nextLine();
			if (!util.validateRole(role))
				return;

			dao.grantRole(id, role);
//		} else {
//			System.out.println("Employee doesn't exist");
//		}
	}

	public void revokeEmpRole(EmployeeDAO dao) {

		System.out.print("Enter emp id to revoke role:");
		String id = sc.next();
		if (!util.validateID(id))
			return;
		
//		if (validations.checkEmpExists(id)) {
			System.out.print("Enter role to revoke:");
			String role = sc.next();
			sc.nextLine();
			if (!util.validateRole(role))
				return;

			dao.revokeRole(id, role);
//		} else {
//			System.out.println("Employee doesn't exist");
//		}
	}
}
