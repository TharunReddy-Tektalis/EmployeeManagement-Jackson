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
	public String generateAutoId() {
		EmployeeFileDAOImpl daoImpl = new EmployeeFileDAOImpl();

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
