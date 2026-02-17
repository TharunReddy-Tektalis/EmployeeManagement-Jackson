package com.employee.services;

import java.util.ArrayList;
import java.util.List;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSRoles;
import com.employee.exception.DataAccessException;
import com.employee.exception.ValidationException;
import com.employee.exception.ServiceException;
import com.employee.model.Employee;
import com.employee.model.UserContext;
import com.employee.util.EmployeeUtil;

public class EmployeeServices {
	EmployeeUtil util = new EmployeeUtil();

	public String addEmployee(EmployeeDAO dao, Employee employee, String role) {
		if (!util.validateName(employee.getName()))
			throw new ValidationException("Invalid Name");
		if (!util.validateDept(employee.getDept()))
			throw new ValidationException("Invalid Department");
		if (!util.validateDOB(employee.getDOB()))
			throw new ValidationException("Invalid DOB");
		if (!util.validateAddress(employee.getAddress()))
			throw new ValidationException("Invalid Address");
		if (!util.validateEmail(employee.getEmail()))
			throw new ValidationException("Invalid Email");
		List<EMSRoles> rolesArray = new ArrayList<>();
		EMSRoles empRole = util.validateRole(role);
		if (empRole == null)
			throw new ValidationException("Invalid Role");
		rolesArray.add(empRole);
		employee.setRole(empRole);
		String password = util.generateRandomPassword();
		String hashPassword = util.generateHash(password);

		try {
			dao.addEmployee(employee.getName(), employee.getDept(), employee.getDOB(), employee.getAddress(),
					employee.getEmail(), rolesArray, hashPassword);
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to add Employee");
		}
		return password;
	}

	public List<Employee> viewAllEmp(EmployeeDAO dao) {
		List<Employee> empList = new ArrayList<>();
		try {
			empList = dao.viewAllEmployees();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to view all employees " + e.getMessage());
		}
		return empList;
	}

	public Employee viewEmpByID(EmployeeDAO dao, UserContext userContext, String id) {
		Employee emp = null;
		if (userContext.isAdminOrManager()) {
			if (!util.validateID(id)) {
				throw new ValidationException("Invalid ID");
			}
			try {
				emp = dao.viewEmployeeById(id);
				if (emp == null) {
					throw new ServiceException("Employee not found");
				}
			} catch (DataAccessException e) {
				throw new ServiceException("Unable to view employee " + e.getMessage());
			}
		} else {
			try {
				emp = dao.viewEmployeeById(id);
			} catch (DataAccessException e) {
				throw new ServiceException("Unable to view employee " + e.getMessage());
			}
		}
		return emp;
	}

	public void deleteEmp(EmployeeDAO dao, String id) {
		if (!util.validateID(id))
			throw new ValidationException("Invalid ID");
		try {
			dao.deleteEmployee(id);
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to delete employee " + e.getMessage());
		}
	}

	public void updateEmp(EmployeeDAO dao, Employee employee, UserContext userContext) {
		if (userContext.isOnlyUser()) {
			if (!util.validateDOB(employee.getDOB()))
				throw new ValidationException("Invalid DOB");
			if (!util.validateAddress(employee.getAddress()))
				throw new ValidationException("Invalid Address");
			if (!util.validateEmail(employee.getEmail()))
				throw new ValidationException("Invalid Email");
			try {
				dao.updateEmployee(employee, EMSRoles.USER);
			} catch (DataAccessException e) {
				throw new ServiceException("Unable to update employee " + e.getMessage());
			}
		} else {
			if (!util.validateID(employee.getId()))
				throw new ValidationException("Invalid Id");
			if (!util.validateName(employee.getName()))
				throw new ValidationException("Invalid Name");
			if (!util.validateDept(employee.getDept()))
				throw new ValidationException("Invalid Department");
			if (!util.validateDOB(employee.getDOB()))
				throw new ValidationException("Invalid DOB");
			if (!util.validateAddress(employee.getAddress()))
				throw new ValidationException("Invalid Address");
			if (!util.validateEmail(employee.getEmail()))
				throw new ValidationException("Invalid Email");
			try {
				dao.updateEmployee(employee, EMSRoles.ADMIN);
			} catch (DataAccessException e) {
				throw new ServiceException("Unable to update employee " + e.getMessage());
			}
		}
	}

	public void grantEmpRole(String id, String role, EmployeeDAO dao) {
		if (!util.validateID(id))
			throw new ValidationException("Invalid Id");
		EMSRoles empRole = util.validateRole(role);
		if (empRole == null)
			throw new ValidationException("Invalid Role");
		try {
			dao.grantRole(id, empRole);
		}catch (DataAccessException e) {
			throw new ServiceException("Unable to grant role" + e.getMessage());
		}
	}
	
	public void revokeEmpRole(String id, String role, EmployeeDAO dao) {
		if (!util.validateID(id))
			throw new ValidationException("Invalid Id");
		EMSRoles empRole = util.validateRole(role);
		if (empRole == null)
			throw new ValidationException("Invalid Role");
		try {
			dao.revokeRole(id, empRole);
		}catch (DataAccessException e) {
			throw new ServiceException("Unable to revoke role" + e.getMessage());
		}
	}
	
	public List<Employee> fetchInactiveEmployees(EmployeeDAO dao){
		List<Employee> empList = new ArrayList<>();
		try {
			empList = dao.fetchInactiveEmployees();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to view all employees " + e.getMessage());
		}
		return empList;
	}
}
