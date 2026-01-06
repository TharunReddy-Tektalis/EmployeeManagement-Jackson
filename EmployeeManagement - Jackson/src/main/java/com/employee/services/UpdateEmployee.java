package com.employee.services;

import java.util.Scanner;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class UpdateEmployee {

	EmployeeDAO dao = new EmployeeDAOImpl();
	ServerSideValidations validations = new ServerSideValidations();
	GetEmployee getEmployee = new GetEmployee();
	EmployeeUtil util = new EmployeeUtil();
	Employee employee = new Employee();
	Scanner sc = new Scanner(System.in);

	public void update() {
		String id;
		if (ServerSideValidations.role.equals("USER")) {
			id = ServerSideValidations.id;
		} else {
			System.out.print("Enter emp id:");
			id = sc.next();
			if(!util.validateID(id)) return;
		}

		boolean present = validations.checkEmployee(id);

		if (present) {
			String dept = "";
			String name = "";
			if (!ServerSideValidations.role.equals("USER")) {
				System.out.print("Enter emp first name:");
				String fname = sc.next();
				sc.nextLine();

				System.out.print("Enter emp last name:");
				String lname = sc.next();
				name = fname + " " + lname;
				if(!util.validateName(name)) return;
				sc.nextLine();

				System.out.print("Enter emp dept:");
				dept = sc.next();
				if(!util.validateDept(dept)) return;
				sc.nextLine();
			}

			System.out.print("Enter emp DOB date:");
			String day = sc.next();
			sc.nextLine();

			System.out.print("Enter emp DOB month:");
			String month = sc.next();
			sc.nextLine();

			System.out.print("Enter emp DOB year:");
			String year = sc.next();
			sc.nextLine();

			String DOB = day + "-" + month + "-" + year;
			if(!util.validateDOB(DOB)) return;

			System.out.print("Enter emp address:");
			String address = sc.nextLine();
			if(!util.validateAddress(address)) return;

			System.out.print("Enter emp email:");
			String email = sc.next();
			if(!util.validateEmail(email)) return;
			sc.nextLine();
			
			dao.updateEmployee(id,name,dept,DOB,address,email);
			if (!ServerSideValidations.role.equals("USER")) {
				getEmployee.get_all(); // SHOW records after every operation
			} else {
				getEmployee.get_by_id();
			}
		}
		else {
			System.out.println("Employee doesn't exist");
		}
	}
}
