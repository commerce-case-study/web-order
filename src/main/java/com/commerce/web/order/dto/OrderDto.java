package com.commerce.web.order.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    private Long id;
    
    private Long memberId;
    
    private String orderCode;
    
    private String status;
    
    private BigDecimal shippingFee;
    
    private BigDecimal totalPrice;
    
    private BigDecimal totalDiscount;
    
    private BigDecimal totalPayment;
    
    private List<OrderItemDto> orderItems;
    
    private OrderDeliveryDto orderDelivery;
    
    private OrderPaymentDto orderPayment;
}
