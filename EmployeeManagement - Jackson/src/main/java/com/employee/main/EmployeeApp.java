package com.employee.main;

import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeDbDAOImpl;
import com.employee.dao.EmployeeFileDAOImpl;
import com.employee.enums.StorageTypes;
import com.employee.storage.StorageSelection;

public class EmployeeApp {
	public static void main(String [] args){
		StorageTypes type = StorageSelection.selectStorageType();
		if(type.equals(StorageTypes.FILE)) {
			EmployeeDAO dao = new EmployeeFileDAOImpl();
			MenuController.displayMenu(dao);
		}
		else {
			EmployeeDAO dao = new EmployeeDbDAOImpl();
			MenuController.displayMenu(dao);
		}
		 // Display menu 
	}
}
