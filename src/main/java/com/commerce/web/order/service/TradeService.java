package com.commerce.web.order.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.web.order.dto.OrderDeliveryDto;
import com.commerce.web.order.dto.OrderDto;
import com.commerce.web.order.dto.OrderItemDto;
import com.commerce.web.order.dto.OrderPaymentDto;
import com.commerce.web.order.dto.ShopDto;

@FeignClient(name = TradeService.SERVICE_NAME)
public interface TradeService {

    public static final String SERVICE_NAME = "service-trade";
    
    @PostMapping(value = "createOrder", consumes = "application/json")
    public Long createOrder(@RequestBody OrderDto order);
    
    @PostMapping(value = "createOrderItem", consumes = "application/json")
    public Long createOrderItem(@RequestBody OrderItemDto orderItem);
    
    @GetMapping(value = "findOrderItemByOrderId/{orderId}", produces = "application/json")
    public List<OrderItemDto> findOrderItemByOrderId(@PathVariable("orderId") Long orderId);
    
    @PostMapping(value = "createOrderPayment", consumes = "application/json")
    public Long createOrderPayment(@RequestBody OrderPaymentDto orderPayment);
    
    @GetMapping(value = "findOrderPaymentByOrderId/{orderId}", produces = "application/json")
    public OrderPaymentDto findOrderPaymentByOrderId(@PathVariable("orderId") Long orderId);
    
    @PostMapping(value = "createOrderDelivery", consumes = "application/json")
    public Long createOrderDelivery(@RequestBody OrderDeliveryDto orderDelivery);
    
    @GetMapping(value = "findOrderDeliveryByOrderId/{orderId}", produces = "application/json")
    public OrderDeliveryDto findOrderDeliveryByOrderId(@PathVariable("orderId") Long orderId);
    
    @GetMapping(value = "findShopById/{shopId}", produces = "application/json")
    public ShopDto findShopById(@PathVariable("shopId") Long id);
    
    @GetMapping(value = "findOrderByOrderCode/{orderCode}", produces = "application/json")
    public OrderDto findOrderByOrderCode(@PathVariable("orderCode") String orderCode);
    
    @GetMapping(value = "findOrderPaymentByPaymentCode/{paymentCode}", produces = "application/json")
    public OrderPaymentDto findOrderPaymentByPaymentCode(@PathVariable("paymentCode") String paymentCode);

    @GetMapping(value = "updateOrderPaymentStatus/{paymentCode}/{status}", produces = "application/json")
    public Boolean updateOrderPaymentStatus(@PathVariable("paymentCode") String paymentCode, @PathVariable("status") String status);

}
