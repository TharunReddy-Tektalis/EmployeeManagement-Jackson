package com.employees.dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employees.exception.EmployeeDoesNotExistException;
import com.employees.exception.InvalidIDException;

public class DeleteEmployee {
	public void delete() {
		CheckEmployees checkEmployees = new CheckEmployees();
		GetEmployee getEmployee = new GetEmployee();
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("Enter empId to delete:");
			String delId = sc.next();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(getEmployee.file));
			JSONArray array = (JSONArray) obj;

			boolean present = checkEmployees.checkEmployee(delId); // Check employee
			if (present) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject jsonObject = (JSONObject) array.get(i);
					String id = (String) jsonObject.get("id");
					if (delId.equals(id)) { // Check id in JSON Array
						array.remove(i); // remove JSON Object from JSON Array
						break;
					}
				}
				FileWriter fw = new FileWriter(getEmployee.file);
				fw.write(array.toJSONString()); // Rewrite JSON File
				fw.flush();
				fw.close();
				System.out.println("Employee Deleted successfully");

				getEmployee.get_all(); // SHOW records after every operation
			} else {
				throw new EmployeeDoesNotExistException("Employee doesn't exist");
			}
		} catch (IOException e) {
			System.out.println("Error regarding File");
		} catch (ParseException e) {
			System.out.println("Parser error");
		}
	}
}
