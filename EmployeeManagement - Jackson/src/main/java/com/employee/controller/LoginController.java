package com.employee.controller;

import java.util.Scanner;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSLoginResult;
import com.employee.exception.InputInvalidException;
import com.employee.exception.ServiceException;
import com.employee.model.EmpLoginResult;
import com.employee.model.UserContext;
import com.employee.services.LoginServices;

public class LoginController {
	LoginServices loginServices = new LoginServices();

	public EmpLoginResult empLoginCheck(EmployeeDAO dao) {
		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("LOGIN");

			System.out.println();

			System.out.print("Enter Employee ID to LOGIN: ");
			String id = sc.next();

			System.out.print("Enter Password to LOGIN: ");
			String password = sc.next();

			try {
				EmpLoginResult empLoginResult = loginServices.empLoginCheck(dao, id, password);
				if (empLoginResult.getLoginResult().equals(EMSLoginResult.SUCCESS)) {
					System.out.println("Login Successful");
					return empLoginResult;
				} else {
					System.out.println("Invalid Credentials, Please try again...");
				}
			} catch (InputInvalidException e) {
				System.out.println("error during login:" + e.getMessage());
			}
		}
	}
	
	public void changePassword(EmployeeDAO dao, UserContext userContext) {
		String id = userContext.getEmpId();
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter new password:");
		String password = sc.next();
		sc.nextLine();
		
		System.out.print("Re-Enter new password:");
		String reEnterPassword = sc.next();
		sc.nextLine();
		
		try {
			loginServices.changePassword(dao,id,password,reEnterPassword);
			System.out.println("Successfully changed password");
		}catch(ServiceException e) {
			System.out.println("Cannot change emplpoyee password "+ e.getMessage());
		}catch (InputInvalidException e) {
			System.out.println("Cannot change emplpoyee password " + e.getMessage());
		}
	}
	
	public void resetPassword(EmployeeDAO dao) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter employee id to reset password:");
		String id = sc.next();
		sc.nextLine();
		try {
			String newDefaultPassword = loginServices.resetPassword(dao,id);
			System.out.println("Password reset succesfully");
			System.out.println("Your reset password:" + newDefaultPassword);
		}
		catch (InputInvalidException | ServiceException e) {	
			System.out.println("Cannot reset password :" + e.getMessage());
		}
	}
}
