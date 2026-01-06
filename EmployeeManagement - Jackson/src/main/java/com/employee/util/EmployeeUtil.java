package com.employee.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.employee.enums.EMSRoles;
import com.employee.model.Employee;

public class EmployeeUtil {
	Employee employee = new Employee();
	public String hash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			byte[] messageDigest = md.digest(password.getBytes());

			BigInteger no = new BigInteger(1, messageDigest);
			String hashPassword = no.toString(16);

			return hashPassword;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error in hashing password");
			return "";
		}
	}

	public boolean validateID(String id) {
		Pattern idPattern = Pattern.compile("tek\\d{1,}");
		Matcher matcher= idPattern.matcher(id);
		if(matcher.matches()) {
			employee.setId(id);
			return true;
		}
		System.out.println("Invalid ID format");
		return false;
	}

	public boolean validateName(String name) {
		if(name==null || name.trim().isEmpty()) {
			System.out.println("Invalid Name format");
			return false;
		}
		employee.setName(name);
		return true;
	}

	public boolean validateDept(String dept) {
		if(dept==null || dept.trim().isEmpty()) {
			System.out.println("Invalid Department format");
			return false;
		}
		employee.setDept(dept);
		return true;
	}

	public boolean validateDOB(String DOB) {
		Pattern dobPattern = Pattern.compile("(0[1-9]|[12][0-9]|3[01])\\-(0[1-9]|1[0-2])\\-\\d{4}");
		Matcher matcher = dobPattern.matcher(DOB);
		if(matcher.matches()) {
			employee.setDOB(DOB);
			return true;
		}
		System.out.println("Invalid DOB format");
		return false;
	}

	public boolean validateAddress(String address) {
		if(address==null || address.trim().isEmpty()) {
			System.out.println("Invalid address format");
			return false;
		}
		employee.setDept(address);
		return true;
	}

	public boolean validateEmail(String email) {
		Pattern emailPattern = Pattern.compile("[A-Za-z0-9.]+@[A-Za-z0-9.]+\\.[A-za-z]{2,}");
		Matcher matcher = emailPattern.matcher(email);
		if(matcher.matches()) {
			employee.setEmail(email);
			return true;
		}
		System.out.println("Invalid Email format");
		return false;
	}

	public boolean validateRole(String role) {
		try {
			EMSRoles choice;
			choice = EMSRoles.valueOf(role.toUpperCase());
			employee.setRole(role);
			return true;
		}
		catch (IllegalArgumentException e) {
			System.out.println("Invalid Role");
		}
		return false;
	}

	public boolean validatePassword(String password) {
		if(password==null || password.trim().isEmpty()) {
			System.out.println("Invalid password format");
			return false;
		}
		employee.setPassword(password);
		return true;
	}
}
