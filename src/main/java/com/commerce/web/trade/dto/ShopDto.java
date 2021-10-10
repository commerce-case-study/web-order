package com.commerce.web.trade.dto;

import lombok.Data;

@Data
public class ShopDto {

    private Long id;
    
    private Long memberAddressId;
    
    private Long shopName;
    
    private String status;
}
