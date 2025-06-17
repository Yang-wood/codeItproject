package com.codeit.mini.util;

import java.util.UUID;

public class CouponCodeGenerator {

	public static String generateCode() {
		return UUID.randomUUID().toString().replaceAll("-", "")
				   .substring(0, 20).toUpperCase();
	}
	
	public static String formatDisplayCode(String rawCode) {
        return String.format("%s-%s-%s-%s",
               rawCode.substring(0, 5),
               rawCode.substring(5, 10),
               rawCode.substring(10, 15),
               rawCode.substring(15, 20));
    }
}
