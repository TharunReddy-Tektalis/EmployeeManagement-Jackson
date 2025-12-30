package com.employee.exception;

public class EmployeeExistsException extends RuntimeException{
	public EmployeeExistsException(String e) {
		super(e);
	}
}
