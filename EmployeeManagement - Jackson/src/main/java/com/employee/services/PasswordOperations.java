package com.employee.services;

import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeFileDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.util.EmployeeUtil;

public class PasswordOperations {
//	EmployeeDAO dao = new EmployeeFileDAOImpl();
	ViewEmpDetails getEmployee = new ViewEmpDetails();
	EmployeeUtil util = new EmployeeUtil();

	public void changePassword(EmployeeDAO dao) {
		String id = MenuController.empLoginResult.getEmpId();
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter new password:");
		String password = sc.next();
		sc.nextLine();
		if (!util.validatePassword(password))
			return;

		System.out.print("Re-Enter new password:");
		String reEnterPassword = sc.next();
		sc.nextLine();
		if (!util.validatePassword(password))
			return;

		if (password.equals(reEnterPassword)) {
			if (util.validatePasswordFormat(password)) {
				String hashPassword = util.generateHash(password);
				dao.changePassword(id, hashPassword);
			} else {
				System.out.println("Invalid Password Format");
			}
		} else {
			System.out.println("Passwords donot match");
		}
	}

	public void resetPassword(EmployeeDAO dao) {
		JSONParser parser = new JSONParser();
		ViewEmpDetails getEmployee = new ViewEmpDetails();
		EmployeeUtil util = new EmployeeUtil();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter employee id to reset password:");
		String id = sc.next();
		sc.nextLine();
		String defPassword = util.generateRandomPassword();
		System.out.println("Newly Reset Password :" + defPassword);
		String hashPassword = util.generateHash(defPassword);
		dao.resetPassword(id, hashPassword);
	}
}
