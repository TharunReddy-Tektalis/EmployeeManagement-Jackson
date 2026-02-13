package com.employee.controller;

import java.util.List;
import java.util.Scanner;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSLoginResult;
import com.employee.enums.EMSOperations;
import com.employee.enums.EMSRoles;
import com.employee.enums.RolePermission;
import com.employee.model.EmpLoginResult;
import com.employee.model.UserContext;

public class MenuController {
	public static void displayMenu(EmployeeDAO dao) {
		RolePermission rolePermission = new RolePermission();
		EmployeeController empController = new EmployeeController();
		LoginController loginController = new LoginController();
		EmpLoginResult empLoginResult = loginController.empLoginCheck(dao);
		UserContext userContext = new UserContext(empLoginResult);
		if (empLoginResult.getLoginResult().equals(EMSLoginResult.SUCCESS)) {

			List<EMSRoles> role = userContext.getEmpRoles();

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

				System.out.println();
				System.out.print("Type your Choice:");
				String input = sc.next().toUpperCase();

				EMSOperations choice = null;
				try {
					choice = EMSOperations.valueOf(input); // Checking whether user entered correct ENUM
				} catch (IllegalArgumentException e) { // Catching exception
					System.out.println("Invalid Menu Choice");
					continue;
				}
				if (!rolePermission.hasAccess(role, choice)) {
					System.out.println("Access denied");
					continue;
				}

				switch (choice) {
				case ADD:
					empController.addEmployee(dao);
					break;
				case VIEW:
					empController.viewAllEmp(dao);
					break;
				case DELETE:
					empController.deleteEmp(dao);
					break;
				case UPDATE:
					empController.updateEmp(dao, userContext);
					break;
				case VIEW_BY_ID:
					empController.viewEmpByID(dao, userContext);
					break;
				case CHANGE_PASSWORD:
					loginController.changePassword(dao, userContext);
					break;
				case RESET_PASSWORD:
					loginController.resetPassword(dao);
					break;
				case GRANT_ROLE:
					empController.grantEmpRole(dao);
					break;
				case REVOKE_ROLE:
					empController.revokeEmpRole(dao);
					break;
				case FETCH_INACTIVE_EMPLOYEES:
					empController.fetchInactiveEmployees(dao);
					break;
				case LOGOUT:
					System.out.println("logout succesfully");
					return;
				case EXIT:
					System.out.println("Exited Employee Management System");
					System.exit(0);
				default:
					System.out.println("Invalid operation");
				}
			}
		}
	}
}