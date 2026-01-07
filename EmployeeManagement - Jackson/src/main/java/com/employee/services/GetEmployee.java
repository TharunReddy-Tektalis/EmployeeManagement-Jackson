package com.employee.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.InvalidIDException;
import com.employee.util.EmployeeUtil;

public class GetEmployee {

	JSONParser parser = new JSONParser();
	EmployeeDAO dao = new EmployeeDAOImpl();
	public static final File file = new File("src/main/resources/employeeDetails.json");

	public void get_all() {

		if (!file.exists() || file.length() <= 2) { // If no Records
			System.out.println("No employees");
			System.out.println();
			return;
		}
		dao.viewEmployee();
	}

	public void get_by_id() {
		EmployeeUtil util = new EmployeeUtil();
		Scanner sc = new Scanner(System.in);
		String id;
		if (CheckLogin.role.equals("USER")) {
			id = CheckLogin.id;
		} else {
			System.out.println("Enter emp id:");
			id = sc.next();
		}
		boolean present = util.checkEmployee(id);

		if (present) {
			dao.viewEmployee_by_id(id);
		} else {
			throw new EmployeeDoesNotExistException("Employee doesn't exist");
		}
	}
}
