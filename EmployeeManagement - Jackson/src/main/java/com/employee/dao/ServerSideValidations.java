package com.employee.dao;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.services.ViewEmpDetails;
import com.employee.util.EmployeeUtil;

public class ServerSideValidations {
	public static String role;
	public static String id;

	public static boolean validateLogin(String id, String password) {
		EmployeeDAOImpl daoImpl = new EmployeeDAOImpl();
		EmployeeUtil util = new EmployeeUtil();

		JSONParser parser = new JSONParser();
		if (!daoImpl.file.exists() || daoImpl.file.length() <= 2) { // If no Records
			System.out.println("No login records");
			System.out.println();
			return false;
		}
		try {
			Object loginData = parser.parse(new FileReader(daoImpl.file));
			JSONArray array = (JSONArray) loginData;

			String hashPassword = util.generateHash(password);

			for (Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;

				String jsonId = (String) jsonObject.get("id");
				String jsonPassword = (String) jsonObject.get("password");

				if (jsonId.equals(id)) { // Check Username
					if (jsonPassword.equals(hashPassword)) { // Check Password
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
		} catch (Exception e) {
			System.out.println("Error in validating login :" + e.getMessage());
			return false;
		}
	}

	public boolean checkEmpExists(String checkId) {

		EmployeeDAOImpl daoImpl = new EmployeeDAOImpl();
		JSONParser parser = new JSONParser();

		try {
			Object empData = parser.parse(new FileReader(daoImpl.file));
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
			System.out.println("Error opening the file :" + e.getMessage());
		} catch (ParseException e) {
			System.out.println("Parser Error :" + e.getMessage());
		}
		return false;
	}

	public String generateAutoId() {
		EmployeeDAOImpl daoImpl = new EmployeeDAOImpl();

		try {
			JSONParser parser = new JSONParser();
			Object empData = parser.parse(new FileReader(daoImpl.file));
			JSONArray array = (JSONArray) empData;

			if (array.size() >= 1) { // Employees exists
				JSONObject jsonObject = (JSONObject) array.get(array.size() - 1);
				String id = (String) jsonObject.get("id"); // Get last employee ID
				int suffix = Integer.parseInt(id.substring(3));
				suffix = suffix + 1;
				return "tek" + suffix; // New id returned
			}
		} catch (ParseException e) {
			System.out.println("Error while ID generation :" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error writing to the file :" + e.getMessage());
		}
		return "tek1"; // Default ID when there are no RECORDS
	}
}
