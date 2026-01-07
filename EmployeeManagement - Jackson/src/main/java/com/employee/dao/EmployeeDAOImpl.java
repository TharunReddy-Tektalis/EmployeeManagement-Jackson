package com.employee.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.employee.main.EmployeeApp;
import com.employee.services.EmployeeLogin;

public class EmployeeDAOImpl implements EmployeeDAO {

//	GetEmployee getEmployee = new GetEmployee();
	ServerSideValidations validations = new ServerSideValidations();
//	EmployeeApp emp = new EmployeeApp();
	File file= EmployeeApp.file;
	JSONParser parser = new JSONParser();

	private void printEmployee(JSONObject jsonObject) {
		System.out.println("Emp ID: " + jsonObject.get("id") + " | Name: " + jsonObject.get("name") + " | Department: "
				+ jsonObject.get("department") + " | DOB: " + jsonObject.get("dob") + " | Address: "
				+ jsonObject.get("address") + " | Email: " + jsonObject.get("email") + " | Roles: "
				+ jsonObject.get("role"));
	}

	private void saveToFile(JSONArray array) throws IOException {
		try (FileWriter fw = new FileWriter(file)) {
			fw.write(array.toJSONString()); // Rewrite JSON File
			fw.flush();
			fw.close();
		}
	}

	private JSONArray getDataFromFile() throws IOException, ParseException {
		if (!file.exists() || file.length() == 0)
			return new JSONArray();
		try (FileReader fr = new FileReader(file)) {
			return (JSONArray) parser.parse(fr);
		}
	}

	public void addEmployee(String name, String dept, String DOB, String address, String email, JSONArray rolesArray,
			String hashPassword) {
		try {
			String id = validations.genId(); // AUTO INCREMENT ID Method
			JSONArray array = getDataFromFile();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", id); // Add ID into JSON Object
			jsonObject.put("name", name); // Add Name into JSON Object
			jsonObject.put("department", dept); // Add Dept into JSON Object
			jsonObject.put("dob", DOB);
			jsonObject.put("address", address);
			jsonObject.put("email", email);
			jsonObject.put("role", rolesArray);
			jsonObject.put("password", hashPassword);

			array.add(jsonObject); // Add JSON Object to JSON Array

			saveToFile(array);
			System.out.println("Employee inserted successfully");

			 // SHOW records after every operation
		} catch (Exception e) {
			System.out.println("Error");
		}
	}

	public void updateEmployee(String id, String name, String dept, String DOB, String address, String email) {
		try {
			JSONArray array = getDataFromFile();
			for (Object object : array) {
				JSONObject jsonObject = (JSONObject) object;
				if (jsonObject.get("id").equals((String) id)) {
					jsonObject.put("name", name); // Add Name into JSON Object
					if (ServerSideValidations.role != "USER") {
						jsonObject.put("department", dept); // Add Dept into JSON Object
					}
					jsonObject.put("dob", DOB); // Add Name into JSON Object
					jsonObject.put("address", address); // Add Dept into JSON Object
					jsonObject.put("email", email); // Add Name into JSON Object

					saveToFile(array);

					System.out.println("Employee Updated successfully");
					System.out.println();
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error");
		}
	}

	public void deleteEmployee(String delId) {
		try {
			JSONArray array = getDataFromFile();
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject = (JSONObject) array.get(i);
				String id = (String) jsonObject.get("id");
				if (delId.equals(id)) { // Check id in JSON Array
					array.remove(i); // remove JSON Object from JSON Array
					break;
				}
			}
			saveToFile(array);
			System.out.println("Employee Deleted successfully");
		} catch (Exception e) {
			System.out.println("Error");
		}
	}

	public void viewEmployee() {
		try {
			JSONArray array = getDataFromFile();

			System.out.println();
			System.out.println("Employee Details");
			System.out.println();
			for (Object obj : array) { // Traversing through JSON Array
				JSONObject jsonObject = (JSONObject) obj;
				printEmployee(jsonObject);
			}
			System.out.println();
		} catch (Exception e) {
			System.out.println("Error");
		}
	}

	public void viewEmployee_by_id(String id) {
		try {
			JSONArray array = getDataFromFile();
			for (Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				String currId = (String) jsonObject.get("id");
				if (id.equals(currId)) { // compare ID from input and json
					System.out.println("Employee Detail");
					printEmployee(jsonObject);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
	
	public void changePassword(String id, String password) {
		try {
			JSONArray array = getDataFromFile();
			for (Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				String currId = (String) jsonObject.get("id");
				if (currId.equals(id)) {
					jsonObject.replace("password", password);
					System.out.println("Successfully changed password");
					saveToFile(array);
					EmployeeLogin.loginCheck();
					break;
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error");
		}
	}
	
	public void resetPassword(String id, String password) {
		try {
			JSONArray array = getDataFromFile();
			for (Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				String currId = (String) jsonObject.get("id");
				if (currId.equals(id)) {
					jsonObject.replace("password", password);
					System.out.println("Successfully changed password to default");
					saveToFile(array);
					break;
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error");
		}
	}
	
	public void grantRole(String id, String role) {
		try {
			JSONArray array = getDataFromFile();
			for(Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				String currId = (String) jsonObject.get("id");
				if(currId.equals(id)) {
					JSONArray roleArray = (JSONArray) jsonObject.get("role");
					if(!roleArray.contains(role)) {
						roleArray.add(role);
						saveToFile(array);
						System.out.println("Employee Updated role");
					}
					else {
						System.out.println("Cannot assign same role again");
					}
				}
			}
			
		}
		catch(Exception e) {
			System.out.println("Error");
		}
	}
	
	public void revokeRole(String id, String role) {
		try {
			JSONArray array = getDataFromFile();
			for (Object obj : array) {
				JSONObject jsonObject = (JSONObject) obj;
				String currId = (String) jsonObject.get("id");
				if (currId.equals(id)) {
					JSONArray roleArray = (JSONArray) jsonObject.get("role");
					if(roleArray.size()>1) {
						if (roleArray.contains(role)) {
							roleArray.remove(role);
							saveToFile(array);
							System.out.println("Revoked employee role");
							
						} else {
							System.out.println("Role doesn't exist");
						}
					}
					else {
						System.out.println("There is should be atleast one role");
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error");
		}
	}
}
