package com.commerce.web.trade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDeliveryDto {

    private Long id;
    
    private Long orderId;
    
    private String courierName;
    
    private String serviceType;
    
    private String awbNumber;
    
    private String senderPhone;
    
    private String senderPostalCode;
    
    private String senderName;
    
    private String senderProvince;
    
    private String senderCity;
    
    private String senderDistrict;
    
    private String senderFullAddress;
    
    private String receiverPhone;
    
    private String receiverPostalCode;
    
    private String receiverName;
    
    private String receiverProvince;
    
    private String receiverCity;
    
    private String receiverDistrict;
    
    private String receiverFullAddress;
}
