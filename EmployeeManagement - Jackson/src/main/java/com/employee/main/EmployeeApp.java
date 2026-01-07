package com.employee.main;

import java.io.File;

import com.employee.controller.MenuController;

public class EmployeeApp {
	public static File file;
	public static void main(String [] args){
		MenuController.displayMenu(); // Display menu 
		if(args.length==0) {
    		return;
    	}
		 file = new File(args[0]);
	}
}
