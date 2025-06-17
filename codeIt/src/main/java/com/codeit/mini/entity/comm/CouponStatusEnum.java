package com.codeit.mini.entity.comm;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum CouponStatusEnum {
	
	ISSUED, USED, EXPIRED;
	
	public boolean isUsable() {
		return this == ISSUED;
	}
}
