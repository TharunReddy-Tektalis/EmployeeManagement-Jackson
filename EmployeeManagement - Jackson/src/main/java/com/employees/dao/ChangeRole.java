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

public class ChangeRole {
	public void grantRole() {
		JSONParser parser = new JSONParser();
		Employee employee = new Employee();
		GetEmployee getEmployee = new GetEmployee();
		Scanner sc = new Scanner(System.in);
		
		if (!getEmployee.file.exists() || getEmployee.file.length() <= 2) { // If no Records
			System.out.println("No employees");
			System.out.println();
			return;
		}
		try {
			Object empData = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) empData;
			
			System.out.print("Enter emp id to grant role:");
			String id = sc.next();
			employee.setId(id);
			
			System.out.print("Enter new role:");
			String role = sc.next();
			sc.nextLine();
			employee.setRole(role);
			
			for(Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				String currId = (String) jsonObject.get("id");
				if(currId.equals(id)) {
					JSONArray roleArray = (JSONArray) jsonObject.get("role");
					if(!roleArray.contains(role)) {
						roleArray.add(role);
						System.out.println("Employee Updated role");
					}
					else {
						System.out.println("Cannot assign same role again");
					}
				}
			}
			FileWriter fw = new FileWriter(getEmployee.file);
			fw.write(array.toJSONString()); // Rewrite JSON File
			fw.flush();
			fw.close();
		}
		catch (ParseException e) {
			System.out.println("Parser error");
		} catch (IOException e) {
			System.out.println("Error writing to the file");
		}
	}
	
	public void revokeRole() {
		JSONParser parser = new JSONParser();
		Employee employee = new Employee();
		GetEmployee getEmployee = new GetEmployee();
		Scanner sc = new Scanner(System.in);
		
		if (!getEmployee.file.exists() || getEmployee.file.length() <= 2) { // If no Records
			System.out.println("No employees");
			System.out.println();
			return;
		}
		try {
			Object empData = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) empData;
			
			System.out.print("Enter emp id to revoke role:");
			String id = sc.next();
			employee.setId(id);
			
			if(array.size()>1) {
				System.out.print("Enter role to revoke:");
				String role = sc.next();
				sc.nextLine();
				employee.setRole(role);
				
				for(Object obj : array) {
					JSONObject jsonObject = (JSONObject) obj;
					String currId = (String) jsonObject.get("id");
					if(currId.equals(id)) {
						JSONArray roleArray = (JSONArray) jsonObject.get("role");
						if(roleArray.contains(role)) {
							roleArray.remove(role);
							System.out.println("Revoked employee role");
						}
						else {
							System.out.println("Role doesn't exist");
						}
					}
				}
			}
			else {
				System.out.println("There is only a single record");
			}
			FileWriter fw = new FileWriter(getEmployee.file);
			fw.write(array.toJSONString()); // Rewrite JSON File
			fw.flush();
			fw.close();
		}
		catch (ParseException e) {
			System.out.println("Parser error");
		} catch (IOException e) {
			System.out.println("Error writing to the file");
		}
	}
}
