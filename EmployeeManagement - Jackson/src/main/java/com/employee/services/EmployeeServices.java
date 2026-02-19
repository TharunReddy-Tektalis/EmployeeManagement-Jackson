package com.employee.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSRoles;
import com.employee.exception.DataAccessException;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.ServiceException;
import com.employee.exception.ValidationException;
import com.employee.model.Employee;
import com.employee.model.UserContext;
import com.employee.util.EmployeeUtil;

public class EmployeeServices {
	EmployeeUtil util = new EmployeeUtil();
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServices.class);
	
	public String addEmployee(EmployeeDAO dao, Employee employee, String role) {
		if (!util.validateName(employee.getName())) {
			logger.warn("invalid name");
			throw new ValidationException("Invalid Name");
		}
		if (!util.validateDept(employee.getDept())) {
			logger.warn("invalid department");
			throw new ValidationException("Invalid Department");
		}
		if (!util.validateDOB(employee.getDOB())) {
			logger.warn("invalid DOB");
			throw new ValidationException("Invalid DOB");
		}
		if (!util.validateAddress(employee.getAddress())) {
			logger.warn("invalid address");
			throw new ValidationException("Invalid Address");
		}
		if (!util.validateEmail(employee.getEmail())) {
			logger.warn("invalid email");
			throw new ValidationException("Invalid Email");
		}
		List<EMSRoles> rolesArray = new ArrayList<>();
		EMSRoles empRole = util.validateRole(role);
		if (empRole == null) {
			logger.warn("invalid role");
			throw new ValidationException("Invalid Role");
		}
		rolesArray.add(empRole);
		employee.setRole(empRole);
		String password = util.generateRandomPassword();
		String hashPassword = util.generateHash(password);

		try {
			dao.addEmployee(employee.getName(), employee.getDept(), employee.getDOB(), employee.getAddress(),
					employee.getEmail(), rolesArray, hashPassword);
			logger.info("successfully added employee");
		} catch (DataAccessException e) {
			logger.error("Unable to add employee {}",e.getMessage());
			throw new ServiceException("Unable to add Employee");
		}
		return password;
	}

	public List<Employee> viewAllEmp(EmployeeDAO dao) {
		List<Employee> empList = new ArrayList<>();
		try {
			empList = dao.viewAllEmployees();
			logger.info("successfully fetched all employees");
		} catch (DataAccessException e) {
			logger.error("Unable to view all employees {}",e.getMessage());
			throw new ServiceException("Unable to view all employees " + e.getMessage());
		}
		return empList;
	}

	public Employee viewEmpByID(EmployeeDAO dao, UserContext userContext, String id) {
		Employee emp = null;
		if (userContext.isAdminOrManager()) {
			if (!util.validateID(id)) {
				logger.warn("invalid ID");
				throw new ValidationException("Invalid ID");
			}
			try {
				emp = dao.viewEmployeeById(id);
				if (emp == null) {
					logger.warn("Employee not found");
					throw new ServiceException("Employee not found");
				}
				logger.info("successfully fetched employee");
			} catch (DataAccessException e) {
				logger.error("Unable to view employee by ID {}",e.getMessage());
				throw new ServiceException("Unable to view employee " + e.getMessage());
			}
			catch(EmployeeDoesNotExistException e) {
				logger.error("Unable to view employee by ID {} ",e.getMessage());
				throw new ServiceException("Unable to view employee " + e.getMessage());
			}
		} else {
			try {
				emp = dao.viewEmployeeById(id);
				logger.info("successfully fetched employee");
			} catch (DataAccessException e) {
				logger.error("Unable to view employee by ID {}",e.getMessage());
				throw new ServiceException("Unable to view employee " + e.getMessage());
			}
		}
		return emp;
	}

	public void deleteEmp(EmployeeDAO dao, String id) {
		if (!util.validateID(id)) {
			logger.warn("invalid ID");
			throw new ValidationException("Invalid ID");
		}
		try {
			dao.deleteEmployee(id);
			logger.info("successfully deleted employee");
		} catch (DataAccessException e) {
			logger.error("Unable to delete employee {}",e.getMessage());
			throw new ServiceException("Unable to delete employee " + e.getMessage());
		}
	}

	public void updateEmp(EmployeeDAO dao, Employee employee, UserContext userContext) {
		if (userContext.isOnlyUser()) {
			if (!util.validateDOB(employee.getDOB())) {
				logger.warn("invalid DOB");
				throw new ValidationException("Invalid DOB");
			}
			if (!util.validateAddress(employee.getAddress())) {
				logger.warn("invalid Address");
				throw new ValidationException("Invalid Address");
			}
			if (!util.validateEmail(employee.getEmail())) {
				logger.warn("invalid Email");
				throw new ValidationException("Invalid Email");
			}
			try {
				dao.updateEmployee(employee, EMSRoles.USER);
				logger.info("successfully updated employee");
			} catch (DataAccessException e) {
				logger.error("Unable to update employee {}",e.getMessage());
				throw new ServiceException("Unable to update employee " + e.getMessage());
			}
		} else {
			if (!util.validateID(employee.getId())) {
				logger.warn("invalid ID");
				throw new ValidationException("Invalid Id");
			}
			if (!util.validateName(employee.getName())) {
				logger.warn("invalid name");
				throw new ValidationException("Invalid Name");
			}
			if (!util.validateDept(employee.getDept())) {
				logger.warn("invalid department");
				throw new ValidationException("Invalid Department");
			}
			if (!util.validateDOB(employee.getDOB())) {
				logger.warn("invalid DOB");
				throw new ValidationException("Invalid DOB");
			}
			if (!util.validateAddress(employee.getAddress())) {
				logger.warn("invalid Address");
				throw new ValidationException("Invalid Address");
			}
			if (!util.validateEmail(employee.getEmail())) {
				logger.warn("invalid email");
				throw new ValidationException("Invalid Email");
			}
			try {
				dao.updateEmployee(employee, EMSRoles.ADMIN);
				logger.info("successfully updated employee");
			} catch (DataAccessException e) {
				logger.error("Unable to update employee {}",e.getMessage());
				throw new ServiceException("Unable to update employee " + e.getMessage());
			}
		}
	}

	public void grantEmpRole(String id, String role, EmployeeDAO dao) {
		if (!util.validateID(id)) {
			logger.warn("invalid ID");
			throw new ValidationException("Invalid Id");
		}
		EMSRoles empRole = util.validateRole(role);
		if (empRole == null) {
			logger.warn("invalid Role");
			throw new ValidationException("Invalid Role");
		}
		try {
			dao.grantRole(id, empRole);
			logger.info("successfully granted employee role");
		}catch (DataAccessException e) {
			logger.error("Unable to grant role to employee {}",e.getMessage());
			throw new ServiceException("Unable to grant role" + e.getMessage());
		}
	}
	
	public void revokeEmpRole(String id, String role, EmployeeDAO dao) {
		if (!util.validateID(id)) {
			logger.warn("invalid ID");
			throw new ValidationException("Invalid Id");
		}
		EMSRoles empRole = util.validateRole(role);
		if (empRole == null) {
			logger.warn("invalid Role");
			throw new ValidationException("Invalid Role");
		}
		try {
			dao.revokeRole(id, empRole);
			logger.info("successfully revoked employee role");
		}catch (DataAccessException e) {
			logger.error("Unable to revoke employee role {}",e.getMessage());
			throw new ServiceException("Unable to revoke role" + e.getMessage());
		}
	}
	
	public List<Employee> fetchInactiveEmployees(EmployeeDAO dao){
		List<Employee> empList = new ArrayList<>();
		try {
			empList = dao.fetchInactiveEmployees();
			logger.info("successfully fetched inactive employees");
		} catch (DataAccessException e) {
			logger.error("Unable to fetch inactive employees {}",e.getMessage());
			throw new ServiceException("Unable to view all employees " + e.getMessage());
		}
		return empList;
	}
}
