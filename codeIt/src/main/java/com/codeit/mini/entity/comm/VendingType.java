package com.codeit.mini.entity.comm;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VendingType {
    RANDOM,CHOICE;
	
	@JsonCreator
	public static VendingType from(String type) {
        try {
            return VendingType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("알 수 없는 자판기 타입입니다: " + type);
        }
    }
	
    public static VendingType fromType(String dbValue) {
        if ("랜덤".equalsIgnoreCase(dbValue)) return RANDOM;
        if ("선택".equalsIgnoreCase(dbValue)) return CHOICE;
        return VendingType.valueOf(dbValue.toUpperCase());
    }
}
