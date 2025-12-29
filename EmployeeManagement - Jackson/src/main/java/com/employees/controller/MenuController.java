package com.employees.controller;

import java.util.Scanner;

import com.employees.dao.AddEmployee;
import com.employees.dao.ChangePassword;
import com.employees.dao.ChangeRole;
import com.employees.dao.CheckLogin;
import com.employees.dao.DeleteEmployee;
import com.employees.dao.GetEmployee;
import com.employees.dao.ResetPassword;
import com.employees.dao.UpdateEmployee;
import com.employees.enums.AdminChoices;
import com.employees.enums.ManagerChoices;
import com.employees.enums.UserChoices;

public class MenuController {
	public static void displayMenu() {
		if (LoginController.loginCheck()) {
			GetEmployee readEmployees = new GetEmployee();
			DeleteEmployee deleteEmployees = new DeleteEmployee();
			UpdateEmployee updateEmployees = new UpdateEmployee();
			ChangePassword changePass = new ChangePassword();
			ResetPassword resetPass = new ResetPassword();
			AddEmployee addEmployee = new AddEmployee();
			ChangeRole changeRole = new ChangeRole();
 			
			boolean exit = false;
			String role = CheckLogin.role;

			Scanner sc = new Scanner(System.in);
			System.out.println();
			System.out.println("EMPLOYEE MANAGEMENT SYSTEM");
			System.out.println();
			while (!exit) { // Runs until user wants to EXIT
				if (role.equals("ADMIN")) {
					System.out.println("ADMIN Operations");
					System.out.println();
					
					for (AdminChoices c : AdminChoices.values()) { // Looping through ENUM Constants
						System.out.println(c);
					}
					
					AdminChoices choice;
					try {
						System.out.println();
						System.out.print("Type your Choice:");
						String input = sc.next();

						choice = AdminChoices.valueOf(input.toUpperCase()); // Checking whether user entered correct
																			// ENUM
						switch (choice) {
							case ADD -> addEmployee.insert();
							case VIEW -> readEmployees.get_all();
							case DELETE -> deleteEmployees.delete();
							case UPDATE -> updateEmployees.update();
							case VIEW_BY_ID -> readEmployees.get_by_id(); // Function call
							case RESET_PASSWORD -> resetPass.resetPassword();							
							case GRANT_ROLE -> changeRole.grantRole();	
							case REVOKE_ROLE -> changeRole.revokeRole();							
							case EXIT -> exit = true;
						}
					} catch (IllegalArgumentException e) { // Catching exception
						System.out.println("Invalid choice");
					}
				} else if (role.equals("MANAGER")) {
					System.out.println("MANAGER Operations");
					System.out.println();
					
					for (ManagerChoices c : ManagerChoices.values()) { // Looping through ENUM Constants
						System.out.println(c);
					}

					ManagerChoices choice;
					try {
						System.out.println();
						System.out.print("Type your Choice:");
						String input = sc.next();
						
						choice = ManagerChoices.valueOf(input.toUpperCase()); // Checking whether user entered correct
																				// ENUM
						switch (choice) {
							case VIEW -> readEmployees.get_all(); // DISPLAYS ALL Records Operation	
							case UPDATE -> updateEmployees.update();	
							case VIEW_BY_ID -> readEmployees.get_by_id(); // Function call
							case EXIT -> exit = true;
						}
					} catch (IllegalArgumentException e) { // Catching exception
						System.out.println("Invalid choice");
					}
				} else {
					System.out.println("USER Operations");
					System.out.println();
					
					for (UserChoices c : UserChoices.values()) { // Looping through ENUM Constants
						System.out.println(c);
					}
		
					UserChoices choice;
					try {
						System.out.println();
						System.out.print("Type your Choice:");
						String input = sc.next();

						choice = UserChoices.valueOf(input.toUpperCase()); // Checking whether user entered correct ENUM

						switch (choice) {
							case VIEW -> readEmployees.get_by_id();
							case CHANGE_PASSWORD -> changePass.changePassword();
							case UPDATE -> updateEmployees.update();
							case EXIT -> exit = true;
						}
					} catch (IllegalArgumentException e) { // Catching exception
						System.out.println("Invalid choice");
					}
				}
			}
		}
	}
}