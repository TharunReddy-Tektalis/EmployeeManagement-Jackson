package com.employee.services;

import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.ServerSideValidations;
import com.employee.enums.EMSRoles;
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
			EMSRoles empRole = util.validateRole(role);
			if (empRole==null)
				return;
			
			dao.grantRole(id, empRole);
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
			EMSRoles empRole = util.validateRole(role);
			if (empRole==null)
				return;

			dao.revokeRole(id, empRole);
//		} else {
//			System.out.println("Employee doesn't exist");
//		}
	}
}
