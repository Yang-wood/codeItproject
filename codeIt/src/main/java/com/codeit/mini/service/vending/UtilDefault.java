package com.codeit.mini.service.vending;

public class UtilDefault {
	
	public static Integer defaultIfNull (Integer value, Integer defaultValue) {
		return value != null ? value : defaultValue;
	}

}
