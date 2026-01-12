package com.employee.storage;

import java.util.Scanner;

import com.employee.enums.StorageTypes;
import com.employee.util.EmployeeUtil;

public class StorageSelection {
	public static StorageTypes selectStorageType() {
		Scanner sc = new Scanner(System.in);
		EmployeeUtil util = new EmployeeUtil();
		StorageTypes type;
		while(true) {
			System.out.println("EMS Storage choices:");
			for(StorageTypes t : StorageTypes.values()){
				System.out.println(t);
			}
			System.out.println("Choose prefered storage type: ");
			String storageType = sc.next().toUpperCase();
			
			try {
				type = StorageTypes.valueOf(storageType);
				break;
			}
			catch(IllegalArgumentException e) {
				System.out.println("Invalid Choice, Please try again...");
			}
		}
		if(type.equals(StorageTypes.DATABASE)) {
			util.startConnection();
		}
		return type;
	}
}
