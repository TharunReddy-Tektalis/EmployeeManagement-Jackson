package com.employee.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.employee.enums.EMSRoles;
import com.employee.model.Employee;

public class EmployeeUtil {
	Employee employee = new Employee();

	public String generateHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] messageDigest = md.digest(password.getBytes());

			BigInteger no = new BigInteger(1, messageDigest);
			String hashPassword = no.toString(16);

			return hashPassword;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such Algorithm exists" + e.getMessage());
			return "";
		}
	}

	public static String generateRandomPassword() {
		String lower = "abcdefghijklmnopqrstuvwxyz";
		String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String specialChar = "!@#$%_*+";
		String digits = "0123456789";
		String allChars = lower + upper + specialChar + digits;

		SecureRandom sr = new SecureRandom();
		List<Character> passwordCharList = new ArrayList<>();

		passwordCharList.add(lower.charAt(sr.nextInt(lower.length())));
		passwordCharList.add(upper.charAt(sr.nextInt(upper.length())));
		passwordCharList.add(specialChar.charAt(sr.nextInt(specialChar.length())));
		passwordCharList.add(digits.charAt(sr.nextInt(digits.length())));
		for (int index = 0; index < 4; index++) {
			passwordCharList.add(allChars.charAt(sr.nextInt(allChars.length())));
		}
		Collections.shuffle(passwordCharList);
		StringBuilder password = new StringBuilder();
		for (char c : passwordCharList) {
			password.append(c);
		}
		return password.toString();
	}

	public boolean validateID(String id) {
		Pattern idPattern = Pattern.compile("tek\\d{1,}");
		Matcher matcher = idPattern.matcher(id);
		if (matcher.matches()) {
			employee.setId(id);
			return true;
		}
		System.out.println("Invalid ID format");
		return false;
	}

	public boolean validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			System.out.println("Invalid Name format");
			return false;
		}
		employee.setName(name);
		return true;
	}

	public boolean validateDept(String dept) {
		if (dept == null || dept.trim().isEmpty()) {
			System.out.println("Invalid Department format");
			return false;
		}
		employee.setDept(dept);
		return true;
	}

	public boolean validateDOB(int day, int month, int year) {
//		Pattern dobPattern = Pattern.compile("(0[1-9]|[12][0-9]|3[01])\\-(0[1-9]|1[0-2])\\-\\d{4}");
//		Matcher matcher = dobPattern.matcher(DOB);
//		if(matcher.matches()) {
//			employee.setDOB(DOB);
//			return true;
//		}
//		System.out.println("Invalid DOB format");
//		return false;

		try {
			LocalDate birthDate = LocalDate.of(year, month, day);
			LocalDate today = LocalDate.now();
			if (birthDate.isAfter(today)) {
				System.out.println("Cannot give DOB in future");
				return false;
			}
			if (birthDate.isBefore(today.minusYears(100))) {
				System.out.println("Invalid DOB Year");
				return false;
			}
			employee.setDOB(birthDate.toString());
			return true;
		} catch (DateTimeException e) {
			System.out.println("Incorrect DOB Format" + e.getMessage());
			return false;
		}
	}

	public boolean validateAddress(String address) {
		if (address == null || address.trim().isEmpty()) {
			System.out.println("Invalid address format");
			return false;
		}
		employee.setDept(address);
		return true;
	}

	public boolean validateEmail(String email) {
		Pattern emailPattern = Pattern.compile("[A-Za-z0-9.]+@[A-Za-z0-9.]+\\.[A-za-z]{2,}");
		Matcher matcher = emailPattern.matcher(email);
		if (matcher.matches()) {
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
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Role");
		}
		return false;
	}

	public boolean validatePassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			System.out.println("Invalid password format");
			return false;
		}
		employee.setPassword(password);
		return true;
	}

	public boolean validatePasswordFormat(String password) {
		Pattern passwordPattern = Pattern
				.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%_*+])[A-Za-z\\d!@#$%_*+]{8,64}$");
		Matcher matcher = passwordPattern.matcher(password);
		if (matcher.matches()) {
			employee.setPassword(password);
			return true;
		}
		return false;
	}

	public Connection startConnection() {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream("src/main/resources/EmsDbConfig.properties")) {
			prop.load(input);

			String url = prop.getProperty("db.url");
			String username = prop.getProperty("db.username");
			String password = prop.getProperty("db.password");

			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection to db successful...");
			return conn;
		} catch (IOException e) {
			System.out.println("Unable to read property file" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Unable to connect to DB" + e.getMessage());
		}
		return null;
	}
}
