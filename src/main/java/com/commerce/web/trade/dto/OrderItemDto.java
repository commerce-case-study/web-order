package com.commerce.web.trade.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {

    private Long id;
    
    private Long orderId;
    
    private Long itemId;
    
    private Long shopId;
    
    private String itemName;
    
    private BigDecimal itemPrice;
    
    private Integer quantity;
}
