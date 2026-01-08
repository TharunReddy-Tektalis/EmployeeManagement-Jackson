package com.employee.services;

import java.util.Scanner;

import com.employee.dao.ServerSideValidations;
import com.employee.util.EmployeeUtil;

public class EmployeeLogin {
	public static boolean empLoginCheck() {

		EmployeeUtil util = new EmployeeUtil();

		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("LOGIN");

			System.out.println();

			System.out.print("Enter Employee ID to LOGIN: ");
			String id = sc.next();
			if (!util.validateID(id))
				return false;

			System.out.print("Enter Password to LOGIN: ");
			String password = sc.next();
			if (!util.validatePassword(password))
				return false;

			if (ServerSideValidations.validateLogin(id, password)) {
				return true;
			} else {
				System.out.println("Invalid Credentials, Please try again...");
			}
		}
	}
}
