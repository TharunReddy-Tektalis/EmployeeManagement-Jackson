package com.employees.dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.controller.LoginController;

public class ResetPassword {
	public static String defaultPass = "pass123";
	public void resetPassword() {
		JSONParser parser = new JSONParser();
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
			
			System.out.print("Enter employee id to reset password:");
			String id = sc.next();
			sc.nextLine();
			String hashPassword = HashPassword.hash(defaultPass);
			for(Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				
				String currId = (String) jsonObject.get("id");
				if(currId.equals(id)) {
					jsonObject.replace("password", hashPassword);
					System.out.println("Successfully changed password to default");
					FileWriter fw = new FileWriter(getEmployee.file);
					fw.write(array.toJSONString()); // Rewrite JSON File
					fw.flush();
					fw.close();
					System.out.println();
					break;
				}
			}
		}
		catch (ParseException e) {
			System.out.println("Parser error");
		} catch (IOException e) {
			System.out.println("Error writing to the file");
		}
	}
}
