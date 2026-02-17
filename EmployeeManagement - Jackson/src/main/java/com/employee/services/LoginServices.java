package com.employee.services;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSLoginResult;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.ValidationException;
import com.employee.model.EmpLoginResult;
import com.employee.util.EmployeeUtil;

public class LoginServices {
	EmployeeUtil util = new EmployeeUtil();

	public EmpLoginResult empLoginCheck(EmployeeDAO dao, String id, String password) {
		if (!util.validateID(id))
			return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
		if (!util.validatePassword(password))
			return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
		String hashPassword = util.generateHash(password);
		try {
			EmpLoginResult empLoginResult = dao.validateLogin(id, hashPassword);
			return empLoginResult;
		} catch (DataAccessException e) {
			throw new ValidationException("Invalid Credentials " + e.getMessage());
		}
	}

	public void changePassword(EmployeeDAO dao, String id, String password, String reEnterPassword) {
		if (!util.validatePassword(password))
			throw new ValidationException("Invalid Password");
		if (password.equals(reEnterPassword)) {
			if (util.validatePasswordFormat(password)) {
				String hashPassword = util.generateHash(password);
				try {
					dao.changePassword(id, hashPassword);
				} catch (DataAccessException e) {
					throw new ValidationException("Cannot Change Password " + e.getMessage());
				}
			} else {
				throw new ValidationException("Invalid Password Format ");
			}
		} else {
			throw new ValidationException("Passwords donot match");
		}
	}
	
	public String resetPassword(EmployeeDAO dao, String id) {
		if (!util.validateID(id))
			throw new ValidationException("Invalid ID");
		try {
			String newDefaultPassword = dao.resetPassword(id);
			if(newDefaultPassword == null)
				throw new ValidationException("Cannot reset password");
			return newDefaultPassword;
		}catch (DataAccessException e) {
			throw new ValidationException(e.getMessage());
		}catch(EmployeeDoesNotExistException e) {
			throw new ValidationException(e.getMessage());
		}
	}
}
