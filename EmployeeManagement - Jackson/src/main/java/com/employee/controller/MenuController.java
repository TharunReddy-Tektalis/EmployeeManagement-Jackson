package com.employee.controller;

import java.util.Scanner;

import com.employee.dao.ServerSideValidations;
import com.employee.enums.EMSOperations;
import com.employee.enums.EMSRoles;
import com.employee.enums.RolePermissions;
import com.employee.services.AddEmployee;
import com.employee.services.ChangeRole;
import com.employee.services.DeleteEmployee;
import com.employee.services.EmployeeLogin;
import com.employee.services.GetEmployee;
import com.employee.services.PasswordOperations;
import com.employee.services.UpdateEmployee;

public class MenuController {
	public static void displayMenu() {
		if (EmployeeLogin.loginCheck()) {
			GetEmployee readEmployees = new GetEmployee();
			DeleteEmployee deleteEmployees = new DeleteEmployee();
			UpdateEmployee updateEmployees = new UpdateEmployee();
			PasswordOperations passwordOperations = new PasswordOperations();
			AddEmployee addEmployee = new AddEmployee();
			ChangeRole changeRole = new ChangeRole();
 			RolePermissions rolePermission = new RolePermissions();
			boolean exit = false;
			String role = ServerSideValidations.role;

			Scanner sc = new Scanner(System.in);
			System.out.println();
			System.out.println("EMPLOYEE MANAGEMENT SYSTEM");
			System.out.println();

			for(EMSOperations op:EMSOperations.values()) {
				if(rolePermission.hasAccess(role, op)) {
					System.out.println(op);
				}
			}
			while(!exit) {
				try {
					System.out.println();
					System.out.print("Type your Choice:");
					String input = sc.next();
					EMSOperations choice;
					choice = EMSOperations.valueOf(input.toUpperCase()); // Checking whether user entered correct ENUM

					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.ADD) addEmployee.insert();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.VIEW) readEmployees.get_all();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.DELETE) deleteEmployees.delete();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.UPDATE) updateEmployees.update();	
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.VIEW_BY_ID) readEmployees.get_by_id();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.CHANGE_PASSWORD) passwordOperations.changePassword();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.RESET_PASSWORD) passwordOperations.resetPassword();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.GRANT_ROLE) changeRole.grantRole();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.REVOKE_ROLE) changeRole.revokeRole();
					if(rolePermission.hasAccess(role, choice) && choice==EMSOperations.EXIT) exit = true; 
				} catch (IllegalArgumentException e) { // Catching exception
					System.out.println("Invalid choice");
				}
			}
		}
	}
}