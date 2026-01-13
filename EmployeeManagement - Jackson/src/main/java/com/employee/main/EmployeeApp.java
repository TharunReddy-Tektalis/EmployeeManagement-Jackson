package com.employee.main;

import java.util.Scanner;

import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDbDAOImpl;
import com.employee.dao.EmployeeFileDAOImpl;
import com.employee.enums.StorageTypes;
import com.employee.util.EmployeeUtil;

public class EmployeeApp {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		EmployeeUtil util = new EmployeeUtil();
		StorageTypes type;
		EmployeeDAO dao;
		while (true) {
			System.out.println("EMS Storage choices:");
			for (StorageTypes t : StorageTypes.values()) {
				System.out.println(t);
			}
			System.out.print("Choose prefered storage type: ");
			String storageType = sc.next().toUpperCase();

			try {
				type = StorageTypes.valueOf(storageType);
				switch (type) {
				case FILE:
					dao = new EmployeeFileDAOImpl();
					MenuController.displayMenu(dao);
					break;

				case DATABASE:
					util.startConnection();
					dao = new EmployeeDbDAOImpl();
					MenuController.displayMenu(dao);
					break;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid Choice, Please try again...");
			}
		}
	}
}
