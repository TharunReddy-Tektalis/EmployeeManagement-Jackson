package com.employee.services;

import java.util.Scanner;

import org.json.simple.JSONArray;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class AddEmployee {

	public void insert() { // INSERT Operation

		GetEmployee getEmployee = new GetEmployee();
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
		employee.setName(name);

		System.out.print("Enter emp dept:");
		String dept = sc.next();
		sc.nextLine();
		employee.setDept(dept);

		System.out.print("Enter emp date in DOB:");
		String day = sc.next();
		sc.nextLine();

		System.out.print("Enter emp month in  DOB:");
		String month = sc.next();
		sc.nextLine();

		System.out.print("Enter emp year in DOB:");
		String year = sc.next();
		sc.nextLine();

		String DOB = day + "-" + month + "-" + year;
		employee.setDOB(DOB);

		System.out.print("Enter emp address:");
		String address = sc.nextLine();
		employee.setAddress(address);

		System.out.print("Enter emp email:");
		String email = sc.next();
		sc.nextLine();
		employee.setEmail(email);

		System.out.print("Enter emp role:");
		String role = sc.next();
		sc.nextLine();

		JSONArray rolesArray = new JSONArray();
		employee.setRole(role);
		rolesArray.add(role);

		String password = PasswordOperations.defaultPass;
		employee.setPassword(password);
		String hashPassword = util.hash(password);

		dao.addEmployee(name,dept,DOB,address,email,rolesArray,hashPassword);
		
		getEmployee.get_all();
	}
}
