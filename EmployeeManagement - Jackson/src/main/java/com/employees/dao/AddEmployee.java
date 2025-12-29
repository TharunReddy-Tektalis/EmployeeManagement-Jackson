package com.employees.dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.dto.Employee;

public class AddEmployee {

	public void insert() { // INSERT Operation

		GetEmployee getEmployee = new GetEmployee(); 
		AutoIDGenerator autoIDGenerator = new AutoIDGenerator();
		Employee employee = new Employee();
		Scanner sc = new Scanner(System.in);

		try {
			System.out.print("Enter emp first name:");
			String fname = sc.next();
			sc.nextLine();
			
			System.out.print("Enter emp last name:");
			String lname = sc.next();
			String name = fname+" "+lname;
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
			
			String DOB = day+"-"+month+"-"+year;
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
			
			String password = ResetPassword.defaultPass;
			employee.setPassword(password);
			String hashPassword = HashPassword.hash(password);
			
			JSONParser parser = new JSONParser(); // JSON Parser
			Object empData = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) empData;

			String id = autoIDGenerator.genId(); // AUTO INCREMENT ID Method

			if (getEmployee.file.exists() && getEmployee.file.length() > 0) {
				Object obj = parser.parse(new FileReader(getEmployee.file));
				array = (JSONArray) obj;
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", id); // Add ID into JSON Object
			jsonObject.put("name", name); // Add Name into JSON Object
			jsonObject.put("department", dept); // Add Dept into JSON Object
			jsonObject.put("dob", DOB);
			jsonObject.put("address", address);
			jsonObject.put("email", email);
			jsonObject.put("role", rolesArray);
			jsonObject.put("password", hashPassword);

			array.add(jsonObject); // Add JSON Object to JSON Array

			FileWriter fw = new FileWriter(getEmployee.file);
			fw.write(array.toJSONString()); // Rewrite JSON File
			fw.close();
			System.out.println("Employee inserted successfully");

			getEmployee.get_all(); // SHOW records after every operation
		} catch (ParseException e) {
			System.out.println("Parser error");
		} catch (IOException e) {
			System.out.println("Error writing to the file");
		}
	}
}
