package com.employee.model;
import com.employee.enums.EMSRoles;

public class Employee {
	private String id;
	private String name;
	private String dept;
	private String dob;
	private String address;
	private String email;
	private EMSRoles role;
	private String password;

	public Employee() {

	}

	public Employee(String name, String dept, String dob, String address, String email) {
		this.name = name;
		this.dept = dept;
		this.dob = dob;
		this.address = address;
		this.email = email;
	}

	public Employee(String id, String name, String dept, String dob, String address, String email) {
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.dob = dob;
		this.address = address;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDept() {
		return dept;
	}

	public String getDOB() {
		return dob;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public EMSRoles getRole() {
		return role;
	}

	public String getPassword() {
		return password;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public void setDOB(String dob) {
		this.dob = dob;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(EMSRoles role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "Emp ID: " + id + " | Name: " + name + " | Department: " + dept + " | DOB: " + dob + " | Address: "
				+ address + " | Email: " + email;
	}
}
