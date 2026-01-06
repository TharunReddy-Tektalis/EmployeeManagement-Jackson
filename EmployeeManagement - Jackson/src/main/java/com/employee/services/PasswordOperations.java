package com.employee.services;

import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.util.EmployeeUtil;

public class PasswordOperations {
	EmployeeDAO dao = new EmployeeDAOImpl();
	GetEmployee getEmployee = new GetEmployee();
	EmployeeUtil util = new EmployeeUtil();
	public static String defaultPass = "pass123";

	public void changePassword() {
		String id = ServerSideValidations.id;
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter new password:");
		String password = sc.next();
		sc.nextLine();
		if(!util.validatePassword(password)) return;

		System.out.print("Re-Enter new password:");
		String samePassword = sc.next();
		sc.nextLine();
		if(!util.validatePassword(password)) return;

		if (password.equals(samePassword)) {
			String hashPassword = util.hash(password);
			dao.changePassword(id, hashPassword);
		} else {
			System.out.println("Passwords donot match");
		}
	}

	public void resetPassword() {
		JSONParser parser = new JSONParser();
		GetEmployee getEmployee = new GetEmployee();
		EmployeeUtil util = new EmployeeUtil();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter employee id to reset password:");
		String id = sc.next();
		sc.nextLine();
		String hashPassword = util.hash(defaultPass);
		dao.resetPassword(id, hashPassword);
	}
}
