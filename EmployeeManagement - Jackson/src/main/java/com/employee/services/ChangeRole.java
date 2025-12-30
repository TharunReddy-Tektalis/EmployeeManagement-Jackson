package com.employee.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class ChangeRole {
	EmployeeDAO dao = new EmployeeDAOImpl();
	EmployeeUtil util = new EmployeeUtil();
	JSONParser parser = new JSONParser();
	Employee employee = new Employee();
	GetEmployee getEmployee = new GetEmployee();
	Scanner sc = new Scanner(System.in);
	
	public void grantRole() {	
		System.out.print("Enter emp id to grant role:");
		String id = sc.next();
		employee.setId(id);
		boolean present = util.checkEmployee(id);
		
		if(present) {
			System.out.print("Enter new role:");
			String role = sc.next();
			sc.nextLine();
			employee.setRole(role);

			dao.grantRole(id, role);
		}
		else {
			System.out.println("Employee doesn't exist");
		}
	}

	public void revokeRole() {

		System.out.print("Enter emp id to revoke role:");
		String id = sc.next();
		employee.setId(id);
		boolean present = util.checkEmployee(id);
		if(present) {
			System.out.print("Enter role to revoke:");
			String role = sc.next();
			sc.nextLine();
			employee.setRole(role);
			
			dao.revokeRole(id, role);
		}
		else {
			System.out.println("Employee doesn't exist");
		}
	}
}
