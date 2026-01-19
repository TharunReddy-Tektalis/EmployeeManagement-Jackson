package com.employee.services;

import java.io.File;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;

import com.employee.controller.MenuController;
import com.employee.dao.EmployeeDAO;
import com.employee.dao.EmployeeFileDAOImpl;
import com.employee.dao.ServerSideValidations;
import com.employee.enums.EMSRoles;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.util.EmployeeUtil;

public class ViewEmpDetails {
	public void viewAllEmp(EmployeeDAO dao) {
		dao.viewAllEmployees();
	}

	public void viewEmpByID(EmployeeDAO dao) {
		ServerSideValidations validations = new ServerSideValidations();
		EmployeeUtil util = new EmployeeUtil();
		Scanner sc = new Scanner(System.in);
		String id;
		if (MenuController.empLoginResult.getEmpRoles().contains(EMSRoles.ADMIN)
				|| MenuController.empLoginResult.getEmpRoles().contains(EMSRoles.MANAGER)) {
			System.out.print("Enter emp id:");
			id = sc.next();
			if (!util.validateID(id))
				return;

		} else {
			id = MenuController.empLoginResult.getEmpId();
		}
		dao.viewEmployeeById(id);
	}
}
