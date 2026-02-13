package com.employee.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
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
				EMSOperations.VIEW_BY_ID,
				EMSOperations.FETCH_INACTIVE_EMPLOYEES,
				EMSOperations.LOGOUT
				));
		map.put("MANAGER", EnumSet.of(
				EMSOperations.EXIT,
				EMSOperations.UPDATE,
				EMSOperations.VIEW,
				EMSOperations.VIEW_BY_ID,
				EMSOperations.LOGOUT
				));
		map.put("USER", EnumSet.of(
				EMSOperations.EXIT,
				EMSOperations.UPDATE,
				EMSOperations.VIEW_BY_ID,
				EMSOperations.CHANGE_PASSWORD,
				EMSOperations.LOGOUT
				));
	}
	
	public boolean hasAccess(List<EMSRoles>roles, EMSOperations operation) {
		for(EMSRoles role : roles) {
			if(map.getOrDefault(role.name(),Collections.emptySet()).contains(operation)) {
				return true;
			}
		}
		return false;
	}
}
