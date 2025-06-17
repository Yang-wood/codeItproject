package com.codeit.mini.dto.vending;

import java.time.LocalDate;
import com.codeit.mini.entity.comm.CouponStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponHistoryRequestDTO extends PageRequest{
	
	private CouponStatusEnum status;
	private LocalDate fromDate;
	private LocalDate toDate;
	
}
