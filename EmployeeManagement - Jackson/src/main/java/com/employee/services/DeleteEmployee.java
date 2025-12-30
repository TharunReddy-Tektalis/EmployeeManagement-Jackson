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
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.util.EmployeeUtil;

public class DeleteEmployee {
	public void delete() {
		EmployeeDAO dao = new EmployeeDAOImpl();
		EmployeeUtil util = new EmployeeUtil();
		GetEmployee getEmployee = new GetEmployee();
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter empId to delete:");
		String delId = sc.next();

		boolean present = util.checkEmployee(delId); // Check employee
		if (present) {
			dao.deleteEmployee(delId);
			getEmployee.get_all(); // SHOW records after every operation
		} else {
			throw new EmployeeDoesNotExistException("Employee doesn't exist");
		}
	}
}
