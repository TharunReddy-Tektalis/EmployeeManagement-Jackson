package com.employee.controller;

import java.util.Scanner;

import com.employee.dao.ServerSideValidations;
import com.employee.enums.EMSOperations;
import com.employee.enums.EMSRoles;
import com.employee.enums.RolePermission;
import com.employee.services.AddEmpDetails;
import com.employee.services.ChangeEmpRole;
import com.employee.services.DeleteEmployee;
import com.employee.services.EmployeeLogin;
import com.employee.services.ViewEmpDetails;
import com.employee.services.PasswordOperations;
import com.employee.services.UpdateEmpDetails;

public class MenuController {

	public static void displayMenu() {
		ViewEmpDetails viewEmpDetails = new ViewEmpDetails();
		DeleteEmployee deleteEmployees = new DeleteEmployee();
		UpdateEmpDetails updateEmployees = new UpdateEmpDetails();
		PasswordOperations passwordOperations = new PasswordOperations();
		AddEmpDetails addEmployee = new AddEmpDetails();
		ChangeEmpRole changeRole = new ChangeEmpRole();
		RolePermission rolePermission = new RolePermission();
		if (EmployeeLogin.empLoginCheck()) {

			String role = ServerSideValidations.role;

			Scanner sc = new Scanner(System.in);
			System.out.println();
			System.out.println("EMPLOYEE MANAGEMENT SYSTEM");
			System.out.println();

			
			while (true) {
				for (EMSOperations op : EMSOperations.values()) {
					if (rolePermission.hasAccess(role, op)) {
						System.out.println(op);
					}
				}
				try {
					System.out.println();
					System.out.print("Type your Choice:");
					String input = sc.next().toUpperCase();
					EMSOperations choice;
					choice = EMSOperations.valueOf(input); // Checking whether user entered correct ENUM

					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.ADD)
						addEmployee.addEmp();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.VIEW)
						viewEmpDetails.viewAllEmp();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.DELETE)
						deleteEmployees.deleteEmp();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.UPDATE)
						updateEmployees.updateEmp();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.VIEW_BY_ID)
						viewEmpDetails.viewEmpByID();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.CHANGE_PASSWORD)
						passwordOperations.changePassword();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.RESET_PASSWORD)
						passwordOperations.resetPassword();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.GRANT_ROLE)
						changeRole.grantEmpRole();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.REVOKE_ROLE)
						changeRole.revokeEmpRole();
					if (rolePermission.hasAccess(role, choice) && choice == EMSOperations.EXIT) {
						System.out.println("Exited Employee Management System");
						break;
					}
				} catch (IllegalArgumentException e) { // Catching exception
					System.out.println("Invalid Menu Choice");
				}
			}
		}
	}
}