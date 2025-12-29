package com.employees.dao;

import java.io.File;
import java.io.FileReader;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CheckLogin {	
	public static String role;
	public static String id;
	public static boolean validateLogin(String id, String password) {
		GetEmployee getEmployee = new GetEmployee();
		
		JSONParser parser = new JSONParser();
		if (!getEmployee.file.exists() || getEmployee.file.length() <=2) { // If no Records
			System.out.println("No login records");
			System.out.println();
			return false;
		}
		try {
			Object loginData = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) loginData;

			String hashPassword = HashPassword.hash(password);
			
			for(Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				
				String jsonId = (String)jsonObject.get("id");
				String jsonPassword = (String)jsonObject.get("password");
				
				if(jsonId.equals(id)) { // Check Username
					if(jsonPassword.equals(hashPassword)) { // Check Password
						System.out.println("Login Successful");
						
						CheckLogin.id = (String) jsonObject.get("id");
						JSONArray roleArray = (JSONArray) jsonObject.get("role");
						
						Comparator<Object> comparator = (obj1, obj2) -> {
							String s1 = (String) obj1;
							String s2 = (String) obj2;
							return s1.compareTo(s2);
						};
						roleArray.sort(comparator);
						
						Object roleObject = roleArray.getFirst();
						CheckLogin.role = (String) roleObject;
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
}
