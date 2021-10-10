package com.commerce.web.trade.dto;

import lombok.Data;

@Data
public class NotificationPaymentDto {

    private String paymentCode;
    
    private String status;
}
