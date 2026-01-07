package com.employee.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDAOImpl;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.InvalidIDException;
import com.employee.model.Employee;
import com.employee.util.EmployeeUtil;

public class UpdateEmployee {

	EmployeeDAO dao = new EmployeeDAOImpl();
	EmployeeUtil util = new EmployeeUtil();
	GetEmployee getEmployee = new GetEmployee();
	Employee employee = new Employee();
	Scanner sc = new Scanner(System.in);

	public void update() {
		String id;
		if (CheckLogin.role.equals("USER")) {
			id = CheckLogin.id;
		} else {
			System.out.println("Enter emp id:");
			id = sc.next();
		}

		boolean present = util.checkEmployee(id);

		if (present) {
			String dept = "";
			String name = "";
			if (!CheckLogin.role.equals("USER")) {
				System.out.print("Enter emp first name:");
				String fname = sc.next();
				sc.nextLine();

				System.out.print("Enter emp last name:");
				String lname = sc.next();
				name = fname + " " + lname;
				employee.setName(name);
				sc.nextLine();

				System.out.print("Enter emp dept:");
				dept = sc.next();
				employee.setDept(dept);
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
			employee.setDOB(DOB);

			System.out.print("Enter emp address:");
			String address = sc.nextLine();
			employee.setAddress(address);

			System.out.print("Enter emp email:");
			String email = sc.next();
			employee.setEmail(email);
			sc.nextLine();
			
			dao.updateEmployee(id,name,dept,DOB,address,email);
			if (!CheckLogin.role.equals("USER")) {
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
