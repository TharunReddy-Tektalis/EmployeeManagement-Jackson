package com.employee.controller;

import java.util.Scanner;

import com.employee.enums.AdminChoices;
import com.employee.enums.ManagerChoices;
import com.employee.enums.UserChoices;
import com.employee.services.AddEmployee;
import com.employee.services.ChangeRole;
import com.employee.services.CheckLogin;
import com.employee.services.DeleteEmployee;
import com.employee.services.GetEmployee;
import com.employee.services.PasswordOperations;
import com.employee.services.UpdateEmployee;

public class MenuController {
	public static void displayMenu() {
		if (LoginController.loginCheck()) {
			GetEmployee readEmployees = new GetEmployee();
			DeleteEmployee deleteEmployees = new DeleteEmployee();
			UpdateEmployee updateEmployees = new UpdateEmployee();
			PasswordOperations passwordOperations = new PasswordOperations();
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
							case RESET_PASSWORD -> passwordOperations.resetPassword();							
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
							case CHANGE_PASSWORD -> passwordOperations.changePassword();
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