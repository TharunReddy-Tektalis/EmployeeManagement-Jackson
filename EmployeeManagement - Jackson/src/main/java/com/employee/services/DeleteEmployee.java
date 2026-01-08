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
import com.employee.dao.ServerSideValidations;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.util.EmployeeUtil;

public class DeleteEmployee {
	public void deleteEmp() {
		EmployeeDAO dao = new EmployeeDAOImpl();
		EmployeeUtil util = new EmployeeUtil();
		ServerSideValidations validations = new ServerSideValidations();
		ViewEmpDetails getEmployee = new ViewEmpDetails();
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter empId to delete:");
		String delId = sc.next();
		if (!util.validateID(delId))
			return;
                                        // Check employee
		if (validations.checkEmpExists(delId)) {
			dao.deleteEmployee(delId);
			getEmployee.viewAllEmp(); // SHOW records after every operation
		} else {
			throw new EmployeeDoesNotExistException("Employee doesn't exist");
		}
	}
}
