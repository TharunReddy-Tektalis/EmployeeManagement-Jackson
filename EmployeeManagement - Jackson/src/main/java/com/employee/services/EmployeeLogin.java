package com.employee.services;

import java.util.Scanner;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSLoginResult;
import com.employee.model.EmpLoginResult;
import com.employee.util.EmployeeUtil;

public class EmployeeLogin {
	public static EmpLoginResult empLoginCheck(EmployeeDAO dao) {

		EmployeeUtil util = new EmployeeUtil();
//		EmployeeDAO dao = new EmployeeFileDAOImpl();
		
		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("LOGIN");

			System.out.println();

			System.out.print("Enter Employee ID to LOGIN: ");
			String id = sc.next();
			
			if (!util.validateID(id))
				return new EmpLoginResult(EMSLoginResult.FAIL, null, null);

			System.out.print("Enter Password to LOGIN: ");
			String password = sc.next();

			String hashPassword = util.generateHash(password);
			if (!util.validatePassword(password))
				return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
			EmpLoginResult empLoginResult = dao.validateLogin(id, hashPassword);
			if (empLoginResult.getLoginResult().equals(EMSLoginResult.SUCCESS)) {
				return empLoginResult;
			} else {
				System.out.println("Invalid Credentials, Please try again...");
			}
		}
	}
}
