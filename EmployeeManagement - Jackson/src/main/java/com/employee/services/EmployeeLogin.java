package com.employee.services;

import java.util.Scanner;

import com.employee.dao.ServerSideValidations;
import com.employee.util.EmployeeUtil;

public class EmployeeLogin {
	public static boolean loginCheck() {
		
		EmployeeUtil util = new EmployeeUtil();
		boolean validUser = false;

		while (!validUser) {
			Scanner sc = new Scanner(System.in);
			System.out.println("LOGIN");

			System.out.println();

			System.out.print("Enter Employee ID to LOGIN: ");
			String id = sc.next();
			if(!util.validateID(id)) return false;

			System.out.print("Enter Password to LOGIN: ");
			String password = sc.next();
			if(!util.validatePassword(password)) return false;
			
			validUser = ServerSideValidations.validateLogin(id, password);

			if (validUser) {
				validUser = false;
				return true;
			}
			System.out.println("Invalid Credentials, Re-Enter Login Credentials");
		}
		return false;
	}
}
