package com.employee.services;

import java.util.Scanner;

import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeFileDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.enums.EMSRoles;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class UpdateEmpDetails {

//	EmployeeDAO dao = new EmployeeFileDAOImpl();
	ServerSideValidations validations = new ServerSideValidations();
	ViewEmpDetails getEmployee = new ViewEmpDetails();
	EmployeeUtil util = new EmployeeUtil();
	Employee employee = new Employee();
	Scanner sc = new Scanner(System.in);

	public void updateEmp(EmployeeDAO dao) {
		String id;
		EMSRoles role = null;
		if (MenuController.empLoginResult.getEmpRoles().contains(EMSRoles.ADMIN)
				|| MenuController.empLoginResult.getEmpRoles().contains(EMSRoles.MANAGER)) {
			role = EMSRoles.ADMIN;
		}
		else {
			role = EMSRoles.USER;
		}
		if (role.equals(EMSRoles.USER)) {
			id = MenuController.empLoginResult.getEmpId();
		} else {
			System.out.print("Enter emp id:");
			id = sc.next();
			if (!util.validateID(id))
				return;
		}

//		if (validations.checkEmpExists(id)) {
			String dept = "";
			String name = "";
			if (!role.equals(EMSRoles.USER)) {
				System.out.print("Enter emp first name:");
				String fname = sc.next();
				sc.nextLine();

				System.out.print("Enter emp last name:");
				String lname = sc.next();
				name = fname + " " + lname;
				if (!util.validateName(name))
					return;
				sc.nextLine();

				System.out.print("Enter emp dept:");
				dept = sc.next();
				if (!util.validateDept(dept))
					return;
				sc.nextLine();
			}

			System.out.print("Enter emp DOB date:");
			int day = sc.nextInt();
			sc.nextLine();

			System.out.print("Enter emp DOB month:");
			int month = sc.nextInt();
			sc.nextLine();

			System.out.print("Enter emp DOB year:");
			int year = sc.nextInt();
			sc.nextLine();

			String DOB = day + "-" + month + "-" + year;
			if (!util.validateDOB(day, month, year))
				return;

			System.out.print("Enter emp address:");
			String address = sc.nextLine();
			if (!util.validateAddress(address))
				return;

			System.out.print("Enter emp email:");
			String email = sc.next();
			if (!util.validateEmail(email))
				return;
			sc.nextLine();

			dao.updateEmployee(id, name, dept, DOB, address, email, role);
			if (!role.equals(EMSRoles.USER)) {
				getEmployee.viewAllEmp(dao); // SHOW records after every operation
			} else {
				getEmployee.viewEmpByID(dao);
			}
//		} else {
//			System.out.println("Employee doesn't exist");
//		}
	}
}
