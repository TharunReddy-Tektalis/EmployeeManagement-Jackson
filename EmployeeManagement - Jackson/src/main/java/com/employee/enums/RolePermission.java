package com.employee.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RolePermission {
	private static final Map<String,Set<EMSOperations>> map = new HashMap<>();
	public RolePermission() {
		
		map.put("ADMIN", EnumSet.of(
				EMSOperations.ADD,
				EMSOperations.DELETE,
				EMSOperations.EXIT,
				EMSOperations.GRANT_ROLE,
				EMSOperations.REVOKE_ROLE,
				EMSOperations.RESET_PASSWORD,
				EMSOperations.UPDATE,
				EMSOperations.VIEW,
				EMSOperations.VIEW_BY_ID
				));
		map.put("MANAGER", EnumSet.of(
				EMSOperations.EXIT,
				EMSOperations.UPDATE,
				EMSOperations.VIEW,
				EMSOperations.VIEW_BY_ID
				));
		map.put("USER", EnumSet.of(
				EMSOperations.EXIT,
				EMSOperations.UPDATE,
				EMSOperations.VIEW_BY_ID,
				EMSOperations.CHANGE_PASSWORD
				));
	}
	
	public boolean hasAccess(String role, EMSOperations operation) {
		return map.get(role).contains(operation);
	}
}
