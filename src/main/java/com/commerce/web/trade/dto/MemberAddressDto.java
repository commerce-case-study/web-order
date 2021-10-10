package com.commerce.web.trade.dto;

import lombok.Data;

@Data
public class MemberAddressDto {

    private Long id;
    
    private Long memberId;
    
    private String name;
    
    private String province;
    
    private String city;
    
    private String district;
    
    private String postalCode;
    
    private String addressLine;
    
    private String notes;
}
