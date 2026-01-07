package com.employee.services;

import java.io.File;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.main.EmployeeApp;
import com.employee.util.EmployeeUtil;

public class GetEmployee {

	JSONParser parser = new JSONParser();
	EmployeeDAO dao = new EmployeeDAOImpl();
	File file= EmployeeApp.file;


	public void get_all() {

		if (!file.exists() || file.length() <= 2) { // If no Records
			System.out.println("No employees");
			System.out.println();
			return;
		}
		dao.viewEmployee();
	}

	public void get_by_id() {
		ServerSideValidations validations = new ServerSideValidations();
		EmployeeUtil util = new EmployeeUtil();
		Scanner sc = new Scanner(System.in);
		String id;
		if (ServerSideValidations.role.equals("USER")) {
			id = ServerSideValidations.id;
		} else {
			System.out.println("Enter emp id:");
			id = sc.next();
			if(!util.validateID(id)) return;
		}
		boolean present = validations.checkEmployee(id);

		if (present) {
			dao.viewEmployee_by_id(id);
		} else {
			throw new EmployeeDoesNotExistException("Employee doesn't exist");
		}
	}
}
