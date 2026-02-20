package com.employee.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.employee.dao.EmployeeDAO;
import com.employee.enums.EMSRoles;
import com.employee.exception.EmployeeDoesNotExistException;
import com.employee.exception.ValidationException;
import com.employee.model.Employee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServicesTest {
	@Mock
	EmployeeDAO employeeDAO;

	@InjectMocks
	EmployeeServices employeeServices;

	@Test
	@DisplayName("add employee should call dao when input is valid")
	void addEmployee_validInput_callsDao() {
		Employee employee = new Employee("Tharun", "Sales", "12-12-2000", "Hyderabad", "tharun@gmail.com");
		employeeServices.addEmployee(employeeDAO, employee, "ADMIN");

		verify(employeeDAO).addEmployee(eq(employee.getName()), eq(employee.getDept()), eq(employee.getDOB()),
				eq(employee.getAddress()), eq(employee.getEmail()), eq(List.of(EMSRoles.ADMIN)), anyString());
	}

	@Test
	@DisplayName("add employee should throw exception for invalid DOB")
	void invalidDob_throwsValidationException() {
		Employee employee = new Employee("Tharun", "Sales", "42-24-2000", "Hyderabad", "tharun@gmail.com");
		ValidationException ex = assertThrows(ValidationException.class, () -> {
			employeeServices.addEmployee(employeeDAO, employee, "ADMIN");
		});
		assertEquals("Invalid DOB", ex.getMessage());
	}

	@Test
	@DisplayName("should throw validation exception when role is null")
	void grantEmpRole_nullRole_throwsValidationException() {
		assertThrows(ValidationException.class, () -> {
			employeeServices.grantEmpRole("asdaa", null, employeeDAO);
		});
	}

	@Test
	@DisplayName("view all emp should return list from DAO")
	void viewAllEmp_returnsEmployeeList() {
		List<Employee> list = new ArrayList<>();
		list.add(new Employee("Tharun", "Sales", "12-12-2000", "Hyderabad", "tharun@gmail.com"));
		when(employeeDAO.viewAllEmployees()).thenReturn(list);
		assertEquals(employeeDAO.viewAllEmployees(), list);
	}

	@Test
	@DisplayName("delete should throw exception for invalid ID")
	void deleteEmp_invalidId_throwsValidationException() {
		assertThrows(ValidationException.class, () -> {
			employeeServices.deleteEmp(employeeDAO, "emp1");
		});
	}

	@Test
	@DisplayName("Employee doesnt exist should throw exception")
	void deleteEmp_employeeDoesNotExist_throwsException() {
		doThrow(new EmployeeDoesNotExistException("No employee")).when(employeeDAO).deleteEmployee(anyString());

		assertThrows(EmployeeDoesNotExistException.class, () -> {
			employeeServices.deleteEmp(employeeDAO, "tek1");
		});
	}
}
