package com.commerce.web.order.dto;

import lombok.Data;

@Data
public class ShopDto {

    private Long id;
    
    private Long memberId;
    
    private Long memberAddressId;
    
    private String shopName;
    
    private String status;
}
