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
import com.employees.exception.EmployeeDoesNotExistException;
import com.employees.exception.InvalidIDException;

public class UpdateEmployee {

	CheckEmployees checkEmployees = new CheckEmployees();
	GetEmployee getEmployee = new GetEmployee();
	Employee employee = new Employee();
	Scanner sc = new Scanner(System.in);
	public void update() {
		try {
			String id;
			if(CheckLogin.role.equals("USER")) {
				id = CheckLogin.id;
			}
			else {
				System.out.println("Enter emp id:");
				id = sc.next();
			}
			JSONParser parser = new JSONParser();

			Object obj = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) obj;
			
			boolean present = checkEmployees.checkEmployee(id);
			
			if(present) {
				String dept= "";
				String name= "";
				if(!CheckLogin.role.equals("USER")) {
					System.out.print("Enter emp first name:");
					String fname = sc.next();
					sc.nextLine();
					
					System.out.print("Enter emp last name:");
					String lname = sc.next();
					name = fname+" "+lname;
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
				
				String DOB = day+"-"+month+"-"+year;
				employee.setDOB(DOB);
				
				System.out.print("Enter emp address:");
				String address = sc.nextLine();
				employee.setAddress(address);
				
				System.out.print("Enter emp email:");
				String email = sc.next();
				employee.setEmail(email);
				sc.nextLine();

				for (Object object : array) {
					JSONObject jsonObject = (JSONObject) object;
					if (jsonObject.get("id").equals((String)id)) {
						jsonObject.put("name", name); // Add Name into JSON Object
						if(CheckLogin.role!="USER") {
							jsonObject.put("department", dept); // Add Dept into JSON Object
						}
						jsonObject.put("dob", DOB); // Add Name into JSON Object
						jsonObject.put("address", address); // Add Dept into JSON Object
						jsonObject.put("email", email); // Add Name into JSON Object
						
						FileWriter fw = new FileWriter(getEmployee.file);
						fw.write(array.toJSONString()); // Rewrite JSON File
						fw.close();
						System.out.println("Employee Updated successfully");
						System.out.println();
						break;
					}
				}
				if(!CheckLogin.role.equals("USER")) {
					getEmployee.get_all(); // SHOW records after every operation
				}
				else {
					getEmployee.get_by_id();
				}
			}
			else {
				throw new EmployeeDoesNotExistException("Employee doesn't exist");
			}
		} 
		catch (IOException e) {
			System.out.println("Error regarding File");
		} 
		catch (ParseException e) {
			System.out.println("Parser error");
		} 
	}
}
