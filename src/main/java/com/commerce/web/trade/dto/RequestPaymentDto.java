package com.commerce.web.trade.dto;

import lombok.Data;

@Data
public class RequestPaymentDto {

    private String orderCode;
    
    private String paymentType;
    
}
