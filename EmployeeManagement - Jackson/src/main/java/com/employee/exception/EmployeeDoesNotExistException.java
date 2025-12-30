package com.employee.exception;

public class EmployeeDoesNotExistException extends RuntimeException{
	public EmployeeDoesNotExistException(String e){
		super(e);
	}
}
