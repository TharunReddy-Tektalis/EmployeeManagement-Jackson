package com.employees.exception;

public class EmployeeExistsException extends RuntimeException{
	public EmployeeExistsException(String e) {
		super(e);
	}
}
