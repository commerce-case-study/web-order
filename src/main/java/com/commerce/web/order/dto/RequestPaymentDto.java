package com.commerce.web.order.dto;

import lombok.Data;

@Data
public class RequestPaymentDto {

    private String orderCode;
    
    private String paymentType;
    
}
