package com.commerce.web.order.dto;

import lombok.Data;

@Data
public class NotificationPaymentDto {

    private String paymentCode;
    
    private String status;
}
