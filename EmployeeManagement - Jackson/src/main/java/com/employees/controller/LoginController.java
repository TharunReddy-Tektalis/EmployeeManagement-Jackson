package com.employees.controller;

import java.util.Scanner;

import com.employees.dao.CheckLogin;

public class LoginController {
	public static boolean loginCheck() {
		
		boolean validUser = false;
		
		while(!validUser) {
			Scanner sc = new Scanner(System.in);
			System.out.println("LOGIN");
			
			System.out.println();
			
			System.out.print("Enter Employee ID to LOGIN: ");
			String id = sc.next();
			
			System.out.print("Enter Password to LOGIN: ");
			String password = sc.next();
						
			validUser = CheckLogin.validateLogin(id, password);
			
			if(validUser) {
				validUser = false;
				return true;
			}
			else {
				System.out.println("Invalid Credentials, Re-Enter Login Credentials");
			}
		}
		return false;
	}
}
