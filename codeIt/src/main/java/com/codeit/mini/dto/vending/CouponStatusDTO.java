package com.codeit.mini.dto.vending;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponStatusDTO {
	
    private Long itemId;
    private String itemName;
    private int issuedCnt;
    private int usedCnt;
    private int expiredCnt;
    
    public int getTotalCount() {
        return issuedCnt + usedCnt + expiredCnt;
    }
}
