package com.employee.model;

import java.util.List;

import com.employee.enums.EMSRoles;

public class UserContext {
	private final EmpLoginResult loginResult;
	
	public UserContext(EmpLoginResult loginResult) {
		if(loginResult == null) {
			throw new IllegalArgumentException("Login result is null");	
		}
		this.loginResult= loginResult;
	}
	
	public EmpLoginResult getLoginResult() {
		return loginResult;
	}
	public String getEmpId() {
		return loginResult.getEmpId();
	}
	public List<EMSRoles> getEmpRoles(){
		return loginResult.getEmpRoles();
	}
	public boolean isAdminOrManager() {
		return loginResult.getEmpRoles().contains(EMSRoles.ADMIN) || loginResult.getEmpRoles().contains(EMSRoles.ADMIN);
	}
	public boolean isOnlyUser() {
		return loginResult.getEmpRoles().size()==1 && loginResult.getEmpRoles().contains(EMSRoles.USER);
	}
}
