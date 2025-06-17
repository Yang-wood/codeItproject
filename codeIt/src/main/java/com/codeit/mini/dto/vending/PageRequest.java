package com.codeit.mini.dto.vending;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
	
    private int page = 0;
    private int size = 20;

    private String type = "issuedDate";
    private String sort = "desc";
    private String keyword;

}
