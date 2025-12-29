package com.employees.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.exception.EmployeeDoesNotExistException;
import com.employees.exception.InvalidIDException;

public class GetEmployee {

	JSONParser parser = new JSONParser();

	public final File file = new File(
			"C:\\Users\\DELL\\eclipse-workspace\\EmployeeManagement - Jackson\\target\\employeeDetails.json");

	public void get_all() {

		if (!file.exists() || file.length() <= 2) { // If no Records
			System.out.println("No employees");
			System.out.println();
			return;
		}
		try {
			Object empData = parser.parse(new FileReader(file));
			JSONArray array = (JSONArray) empData;

			System.out.println();
			System.out.println("Employee Details");
			System.out.println();
			for (Object obj : array) { // Traversing through JSON Array
				JSONObject jsonObject = (JSONObject) obj;

				System.out.println("Emp ID: " + jsonObject.get("id") + " | Name: " + jsonObject.get("name")
						+ " | Department: " + jsonObject.get("department") + " | DOB: " + jsonObject.get("dob")
						+ " | Address: " + jsonObject.get("address") + " | Email: " + jsonObject.get("email")
						+ " | Roles: " + jsonObject.get("role"));
			}
			System.out.println();
		} catch (IOException e) {
			System.out.println("Error regarding File");
		} catch (ParseException e) {
			System.out.println("Parser error");
		}
	}
	
	public void get_by_id() {
		CheckEmployees check = new CheckEmployees();
		Scanner sc = new Scanner(System.in);
		try {
			String id;
			if(CheckLogin.role.equals("USER")) {
				id = CheckLogin.id;
			}
			else {
				System.out.println("Enter emp id:");
				id = sc.next();
			}
			boolean present = check.checkEmployee(id);
			Object empData = parser.parse(new FileReader(file));
			JSONArray array = (JSONArray) empData;
			if (present) {
				for (Object obj : array) {
					JSONObject jsonObject = (JSONObject) obj;
					String currId = (String) jsonObject.get("id");
					if (id.equals(currId)) { // compare ID from input and json
						System.out.println("Employee Detail");

						System.out.println("Emp ID: " + jsonObject.get("id") + " | Name: " + jsonObject.get("name")
						+ " | Department: " + jsonObject.get("department") + " | DOB: " + jsonObject.get("dob")
						+ " | Address: " + jsonObject.get("address") + " | Email: " + jsonObject.get("email")
						+ " | Roles: " + jsonObject.get("role"));
						System.out.println();
						break;
					}
				}
			} else {
				throw new EmployeeDoesNotExistException("Employee doesn't exist");
			}
		} catch (IOException e) {
			System.out.println("Error");
		} catch (ParseException e) {
			System.out.println("Parser Error");
		}
	}
}
