package com.employee.services;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.employee.enums.EMSRoles;
import com.employee.util.EmployeeUtil;

class EmployeeUtilTest {
	private EmployeeUtil util;

	@BeforeEach
	void setup() {
		util = new EmployeeUtil();
	}

	// Name Validation
	@Test
	void validateNameShouldReturnTrueForValidName() {
		assertTrue(util.validateName("Shanmukh"));
	}

	@Test
	void validateNameShouldReturnFalseForEmptyName() {
		assertFalse(util.validateName(""));
	}

	@Test
	void validateNameShouldReturnFalseForNullName() {
		assertFalse(util.validateName(null));
	}

	// DOB Validation
	@Test
	void validateDOBShouldReturnTrueForValidDOB() {
		assertTrue(util.validateDOB(8, 9, 2005));
	}

	@Test
	void validateDOBShouldReturnFalseForFutureDOB() {
		assertFalse(util.validateDOB(8, 9, 2055));
	}

	@Test
	void validateDOBShouldReturnFalseForMinusHundredYearDOB() {
		assertFalse(util.validateDOB(8, 9, 1905));
	}

	@Test
	void validateDOBShouldReturnFalseForInvalidDOB() {
		assertFalse(util.validateDOB(2005, 9, 8));
	}

	@Test
	void validateDOBShouldReturnTrueForValidLeapYearDOB() {
		assertTrue(util.validateDOB(29, 2, 2024));
	}

	@Test
	void validateDOBShouldReturnFalseForInvalidLeapYearDOB() {
		assertFalse(util.validateDOB(29, 2, 2023));
	}

	// Role Validation
	@Test
	void validateRoleShouldReturnTrueForValidRole() {
		assertEquals(EMSRoles.ADMIN, util.validateRole("ADMIN"));
	}

	@Test
	void validateRoleShouldReturnTrueForLowerCaseValidRole() {
		assertEquals(EMSRoles.ADMIN, util.validateRole("admin"));
	}

	@Test
	void validateRoleShouldReturnFalseForInvalidRole() {
		assertNull(util.validateRole("shanumkh"));
	}

	// Password Format Validation
	@Test
	void validatePasswordShouldReturnTrueForValidPasswordFormat() {
		assertTrue(util.validatePasswordFormat("Tek@1234"));
	}

	@Test
	void validatePasswordShouldReturnFalseForInvalidPasswordFormat() {
		assertFalse(util.validatePasswordFormat("password"));
	}

	@Test
	void validatePasswordShouldReturnFalseForInvalidPasswordLength() {
		assertFalse(util.validatePasswordFormat("Tek@1"));
	}

	// Email Validation
	@Test
	void validateEmailShouldReturnTrueForValidEmail() {
		assertTrue(util.validateEmail("tharun@tektalis.com"));
	}

	@Test
	void validateEmailShouldReturnFalseForInvalidEmail() {
		assertFalse(util.validateEmail("email.com"));
	}

	@Test
	void validateEmailShouldReturnFalseForInvalidEmailCharacters() {
		assertFalse(util.validateEmail("tharun$*243@gmail.uk"));
	}

	// ID Validation
	@Test
	void validateIDShouldReturnTrueForValidID() {
		assertTrue(util.validateID("tek12"));
	}

	@Test
	void validateIDShouldReturnFalseForInvalidID() {
		assertFalse(util.validateID("12"));
	}

	// Random Password
	@Test
	void generateRandomPasswordShouldReturnTrueForValidPasswordFormat() {
		assertTrue(util.validatePasswordFormat(util.generateRandomPassword()));
	}

	@Test
	void generateRandomPasswordShouldReturnTrueForValidPasswordLength() {
		assertEquals(8, util.generateRandomPassword().length());
	}

	// Hash Password
	@Test
	void generateHashShouldReturnSameHashForSameInput() {
		assertEquals(util.generateHash("password"), util.generateHash("password"));
	}
	
	@Test
	void generateHashShouldReturnNullHashForNullInput() {
		assertNull(util.generateHash(null));
	}
	
	@Test
	void generateHashShouldReturnNullHashForEmptyInput() {
		assertNull(util.generateHash(""));
	}
}
