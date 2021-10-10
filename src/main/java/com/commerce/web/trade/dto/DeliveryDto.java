package com.commerce.web.trade.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryDto {
    
    private String courierName;
    
    private String serviceType;
    
    private BigDecimal shippingFee;
    
    private String awbNumber;
    
    private String senderName;
    
    private String senderProvince;
    
    private String senderCity;
    
    private String senderDistrict;
    
    private String senderPostalCode;
    
    private String senderFullAddress;
    
    private String receiverName;
    
    private String receiverProvince;
    
    private String receiverCity;
    
    private String receiverDistrict;
    
    private String receiverPostalCode;
    
    private String receiverFullAddress;
}
