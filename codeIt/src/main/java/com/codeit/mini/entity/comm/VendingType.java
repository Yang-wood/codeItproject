package com.codeit.mini.entity.comm;

public enum VendingType {
    RANDOM, CHOICE;
	
	public static VendingType from(String type) {
        try {
            return VendingType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("알 수 없는 자판기 타입입니다: " + type);
        }
    }
}
