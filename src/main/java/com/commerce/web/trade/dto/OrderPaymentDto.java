package com.commerce.web.trade.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPaymentDto {

    private Long id;
    
    private Long orderId;
    
    private String paymentCode;
    
    private String status;
    
    private BigDecimal amount;
}
