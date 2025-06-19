package com.codeit.mini.dto.comm;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UnifiedCouponHistoryDTO {
	
    private String type;
    private String couponCode;
    private String itemName;
    private LocalDateTime issuedDate;
    private LocalDateTime expireDate;
    private String status;
    
}
