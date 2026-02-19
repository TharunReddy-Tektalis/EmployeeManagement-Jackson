package com.employee.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSLoginResult;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.ValidationException;
import com.employee.model.EmpLoginResult;
import com.employee.util.EmployeeUtil;

public class LoginServices {
	EmployeeUtil util = new EmployeeUtil();
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServices.class);
	
	public EmpLoginResult empLoginCheck(EmployeeDAO dao, String id, String password) {
		if (!util.validateID(id)) {
			logger.warn("invalid ID");
			return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
		}
		if (!util.validatePassword(password)) {
			logger.warn("invalid password");
			return new EmpLoginResult(EMSLoginResult.FAIL, null, null);
		}
		String hashPassword = util.generateHash(password);
		try {
			EmpLoginResult empLoginResult = dao.validateLogin(id, hashPassword);
			if(empLoginResult.getLoginResult()==EMSLoginResult.SUCCESS) {
				logger.info("successful login");
				return empLoginResult;
			}
			logger.warn("Invalid credentials");
			return empLoginResult;
		} catch (DataAccessException e) {
			logger.error("Unable to login {}",e.getMessage());
			throw new ValidationException("Invalid Credentials " + e.getMessage());
		}
	}

	public void changePassword(EmployeeDAO dao, String id, String password, String reEnterPassword) {
		if (!util.validatePassword(password)) {
			logger.warn("invalid password");
			throw new ValidationException("Invalid Password");
		}
		if (password.equals(reEnterPassword)) {
			if (util.validatePasswordFormat(password)) {
				String hashPassword = util.generateHash(password);
				try {
					dao.changePassword(id, hashPassword);
					logger.info("successfully changed password");
				} catch (DataAccessException e) {
					logger.error("Unable to change password {}",e.getMessage());
					throw new ValidationException("Cannot Change Password " + e.getMessage());
				}
			} else {
				logger.warn("invalid password format");
				throw new ValidationException("Invalid Password Format ");
			}
		} else {
			logger.warn("invalid password");
			throw new ValidationException("Passwords donot match");
		}
	}
	
	public String resetPassword(EmployeeDAO dao, String id) {
		if (!util.validateID(id)) {
			logger.warn("invalid ID");
			throw new ValidationException("Invalid ID");
		}
		try {
			String newDefaultPassword = dao.resetPassword(id);
			if(newDefaultPassword == null) {
				logger.warn("invalid default password returned");
				throw new ValidationException("Cannot reset password");
			}
			logger.info("successfully reset password");
			return newDefaultPassword;
		}catch (DataAccessException e) {
			logger.error("Unable to reset password {}",e.getMessage());
			throw new ValidationException(e.getMessage());
		}catch(EmployeeDoesNotExistException e) {
			logger.error("Unable to reset password {}",e.getMessage());
			throw new ValidationException(e.getMessage());
		}
	}
}
