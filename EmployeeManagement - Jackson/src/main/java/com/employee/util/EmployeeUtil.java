package com.employee.util;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.services.GetEmployee;

public class EmployeeUtil {
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
	
	
	public String hash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			
			byte[] messageDigest = md.digest(password.getBytes());
			
			BigInteger no = new BigInteger(1,messageDigest);
			String hashPassword = no.toString(16);
			
			return hashPassword;
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println("Error in hashing password");
			return "";
		}
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
