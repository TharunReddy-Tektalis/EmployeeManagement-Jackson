package com.employee.services;

import java.io.File;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.util.EmployeeUtil;

public class ViewEmpDetails {

	JSONParser parser = new JSONParser();
	EmployeeDAO dao = new EmployeeDAOImpl();

	public void viewAllEmp() {
		dao.view_all_Employees();
	}

	public void viewEmpByID() {
		ServerSideValidations validations = new ServerSideValidations();
		EmployeeUtil util = new EmployeeUtil();
		Scanner sc = new Scanner(System.in);
		String id;
		if (ServerSideValidations.role.equals("USER")) {
			id = ServerSideValidations.id;
		} else {
			System.out.println("Enter emp id:");
			id = sc.next();
			if (!util.validateID(id))
				return;
		}

		if (validations.checkEmpExists(id)) {
			dao.viewEmployee_by_id(id);
		} else {
			throw new EmployeeDoesNotExistException("Employee doesn't exist");
		}
	}
}
