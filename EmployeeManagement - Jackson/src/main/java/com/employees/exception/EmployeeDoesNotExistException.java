package com.employees.exception;

public class EmployeeDoesNotExistException extends RuntimeException{
	public EmployeeDoesNotExistException(String e){
		super(e);
	}
}
