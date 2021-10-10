package com.commerce.web.order.dto;

import java.util.List;

import lombok.Data;

@Data
public class RequestOrderDto {

    private List<RequestOrderItemDto> items;
    
    private Long memberAddressId;
    
    private Long shopId;
    
    private String paymentType;
    
    private String courierName;
    
    private String courierServiceType;
}
