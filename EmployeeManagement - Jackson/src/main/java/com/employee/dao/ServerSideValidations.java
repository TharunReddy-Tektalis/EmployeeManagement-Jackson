package com.employee.dao;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.services.GetEmployee;
import com.employee.util.EmployeeUtil;

public class ServerSideValidations {	
	public static String role;
	public static String id;
	
	public static boolean validateLogin(String id, String password) {
		GetEmployee getEmployee = new GetEmployee();
		EmployeeUtil util = new EmployeeUtil();
		
		JSONParser parser = new JSONParser();
		if (!getEmployee.file.exists() || getEmployee.file.length() <=2) { // If no Records
			System.out.println("No login records");
			System.out.println();
			return false;
		}
		try {
			Object loginData = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) loginData;

			String hashPassword = util.hash(password);
			
			for(Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				
				String jsonId = (String)jsonObject.get("id");
				String jsonPassword = (String)jsonObject.get("password");
				
				if(jsonId.equals(id)) { // Check Username
					if(jsonPassword.equals(hashPassword)) { // Check Password
						System.out.println("Login Successful");
						
						ServerSideValidations.id = (String) jsonObject.get("id");
						JSONArray roleArray = (JSONArray) jsonObject.get("role");
						
						Comparator<Object> comparator = (obj1, obj2) -> {
							String s1 = (String) obj1;
							String s2 = (String) obj2;
							return s1.compareTo(s2);
						};
						roleArray.sort(comparator);
						
						Object roleObject = roleArray.getFirst();
						ServerSideValidations.role = (String) roleObject;
						return true;
					}
					return false;
				}
			}
			return false;
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean checkEmployee(String checkId) {

		GetEmployee viewEmployees = new GetEmployee();
		JSONParser parser = new JSONParser();

		try {
			Object empData = parser.parse(new FileReader(viewEmployees.file));
			JSONArray array = (JSONArray) empData;

			for (Object obj : array) { // Traverse through JSON Array
				JSONObject jsonObject = (JSONObject) obj;
				String id = (String) jsonObject.get("id");
				if (id.equals(checkId)) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			System.out.println("Error");
		} catch (ParseException e) {
			System.out.println("Parser Error");
		}
		return false;
	}
	
	public String genId() {
		GetEmployee readEmployees = new GetEmployee();

		try {
			JSONParser parser = new JSONParser();
			Object empData = parser.parse(new FileReader(readEmployees.file));
			JSONArray array = (JSONArray) empData;

			int len = array.size(); // JSON Array length
			if (len >= 1) { // Employees exists
				JSONObject jsonObject = (JSONObject) array.get(len - 1);
				String id = (String) jsonObject.get("id"); // Get last employee ID
				int suffix = Integer.parseInt(id.substring(3));
				suffix = suffix + 1;
				return "tek" + suffix; // New id returned
			}
		} catch (ParseException e) {
			System.out.println("Parser error");
		} catch (IOException e) {
			System.out.println("Error writing to the file");
		}
		return "tek1"; // Default ID when there are no RECORDS
	}
}
