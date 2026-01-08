package com.employee.services;

import java.util.Scanner;

import org.json.simple.JSONArray;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class AddEmpDetails {

	public void addEmp() { // INSERT Operation

		ViewEmpDetails getEmployee = new ViewEmpDetails();
		EmployeeDAO dao = new EmployeeDAOImpl();
		EmployeeUtil util = new EmployeeUtil();
		Employee employee = new Employee();
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter emp first name:");
		String fname = sc.next();
		sc.nextLine();

		System.out.print("Enter emp last name:");
		String lname = sc.next();
		String name = fname + " " + lname;
		sc.nextLine();
		if (!util.validateName(name))
			return;

		System.out.print("Enter emp dept:");
		String dept = sc.next();
		sc.nextLine();
		if (!util.validateDept(dept))
			return;

		System.out.print("Enter emp date in DOB:");
		int day = sc.nextInt();
		sc.nextLine();

		System.out.print("Enter emp month in  DOB:");
		int month = sc.nextInt();
		sc.nextLine();

		System.out.print("Enter emp year in DOB:");
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
		sc.nextLine();
		if (!util.validateEmail(email))
			return;

		System.out.print("Enter emp role:");
		String role = sc.next();
		sc.nextLine();

		JSONArray rolesArray = new JSONArray();
		if (!util.validateRole(role))
			return;
		rolesArray.add(role);

		String password = util.generateRandomPassword();
		System.out.println("Randomly Generated Default Password: " + password);
		if (!util.validatePassword(password))
			return;
		String hashPassword = util.generateHash(password);

		dao.addEmployee(name, dept, DOB, address, email, rolesArray, hashPassword);

		getEmployee.viewAllEmp();
	}
}
