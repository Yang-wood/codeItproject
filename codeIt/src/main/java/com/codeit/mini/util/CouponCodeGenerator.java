package com.codeit.mini.util;

import java.util.UUID;

public class CouponCodeGenerator {

	public static String generateCode() {
		return UUID.randomUUID().toString().substring(0, 14).toUpperCase();
	}
}
