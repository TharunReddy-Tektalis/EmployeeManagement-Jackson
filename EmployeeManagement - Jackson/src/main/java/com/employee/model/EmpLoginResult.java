package com.employee.model;

import java.util.List;

import com.employee.enums.EMSLoginResult;
import com.employee.enums.EMSRoles;

public class EmpLoginResult {
	private EMSLoginResult loginResult;
	private String empId;
	private List<EMSRoles> empRoles;

	public void setLoginResult(EMSLoginResult loginResult) {
		this.loginResult = loginResult;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public void setEmpRoles(List<EMSRoles> empRoles) {
		this.empRoles = empRoles;
	}
	
	public EmpLoginResult(EMSLoginResult loginResult, String empId, List<EMSRoles> empRoles) {
		this.loginResult = loginResult;
		this.empId = empId;
		this.empRoles = empRoles;
	}
	public EMSLoginResult getLoginResult() {
		return loginResult;
	}
	public String getEmpId() {
		return empId;
	}
	public List<EMSRoles> getEmpRoles() {
		return empRoles;
	}
}
