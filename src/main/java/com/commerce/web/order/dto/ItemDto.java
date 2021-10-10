package com.commerce.web.order.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemDto {

    private Long id;
    
    private Long shopId;
    
    private String name;
    
    private BigDecimal price;
    
    private Integer quantity;
}
