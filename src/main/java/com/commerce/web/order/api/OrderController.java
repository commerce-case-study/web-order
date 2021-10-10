package com.commerce.web.order.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.lib.JsonConverterUtil;
import com.commerce.web.order.config.annotation.NeedLogin;
import com.commerce.web.order.dto.DeliveryDto;
import com.commerce.web.order.dto.ItemDto;
import com.commerce.web.order.dto.MemberAddressDto;
import com.commerce.web.order.dto.MemberDetail;
import com.commerce.web.order.dto.MemberDto;
import com.commerce.web.order.dto.OrderDeliveryDto;
import com.commerce.web.order.dto.OrderDto;
import com.commerce.web.order.dto.OrderItemDto;
import com.commerce.web.order.dto.RequestOrderDto;
import com.commerce.web.order.dto.RequestOrderItemDto;
import com.commerce.web.order.dto.ShopDto;
import com.commerce.web.order.enums.OrderStatus;
import com.commerce.web.order.service.MemberService;
import com.commerce.web.order.service.ProductService;
import com.commerce.web.order.service.TradeService;

@RestController
@RequestMapping("/api/microsite/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    TradeService tradeService;
    
    @Autowired
    MemberService memberService;
    
    @Autowired
    ProductService productService;
    
    /**
     * Generate Order Code
     * @return
     */
    private String generateOrderCode() {
        return "ORD"+System.currentTimeMillis();
    }
    
    private DeliveryDto getDelivery(String courier, String serviceType, MemberAddressDto sender, MemberAddressDto receiver) {
        return DeliveryDto.builder()
                .courierName(courier)
                .serviceType(serviceType)
                // Sender
                .senderName(sender.getName())
                .senderProvince(sender.getProvince())
                .senderCity(sender.getCity())
                .senderDistrict(sender.getDistrict())
                .senderFullAddress(sender.getAddressLine())
                .senderPostalCode(sender.getPostalCode())
                // Receiver
                .receiverName(receiver.getName())
                .receiverProvince(receiver.getProvince())
                .receiverCity(receiver.getCity())
                .receiverDistrict(receiver.getDistrict())
                .receiverFullAddress(receiver.getAddressLine())
                .receiverPostalCode(receiver.getPostalCode())
                // Response From Courier Provider API
                .awbNumber("AWB9987765543121")
                .shippingFee(BigDecimal.valueOf(9000))
                .build();
    }
    
    @NeedLogin
    @GetMapping(value = "/{orderCode}", produces = "application/json")
    public ResponseEntity<String> getOrderDetail(MemberDetail memberDetail, @PathVariable("orderCode") String orderCode) {
        OrderDto order = tradeService.findOrderByOrderCode(orderCode);
        if(null == order) {
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("message", "error, order not found");
            return new ResponseEntity<String>(
                    JsonConverterUtil.convertObjectToJson(maps), 
                    HttpStatus.BAD_REQUEST);
        }
        
        MemberDto member = memberService.findByUsername(memberDetail.getUserName());
        if(order.getMemberId() != member.getId()) {
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("message", "error, order not found for selected member");
            return new ResponseEntity<String>(
                    JsonConverterUtil.convertObjectToJson(maps), 
                    HttpStatus.BAD_REQUEST);
        }
        
        // Set Order Delivery
        order.setOrderDelivery(tradeService.findOrderDeliveryByOrderId(order.getId()));
        // Set Order Items
        order.setOrderItems(tradeService.findOrderItemByOrderId(order.getId()));
        // Set Order Payment
        order.setOrderPayment(tradeService.findOrderPaymentByOrderId(order.getId()));
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(order), 
                HttpStatus.OK);
    }
    
    @NeedLogin
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createOrder(MemberDetail memberDetail, @RequestBody RequestOrderDto requestOrder) {
        
        // Gather User Detail based on Member Detail Username
        MemberDto memberDto = memberService.findByUsername(memberDetail.getUserName());
        
        // 0-A. Calculate Shipping Fee from DeliveryDto
        // -- Sender
        MemberAddressDto sender = memberService.findMemberAddressById(requestOrder.getMemberAddressId());
        // -- Receiver
        ShopDto shop = tradeService.findShopById(requestOrder.getShopId());
        MemberAddressDto receiver = memberService.findMemberAddressById(shop.getMemberAddressId());
        
        DeliveryDto deliveryDto = getDelivery(
                                        requestOrder.getCourierName(), 
                                        requestOrder.getCourierServiceType(), sender, receiver); 
        BigDecimal shippingFee  = deliveryDto.getShippingFee();
        
        // 0-B. Calculate Discount (currently is always return Rp. 0)
        BigDecimal totalDiscount = BigDecimal.ZERO;
        
        // 0-C. Calculate Total Item Price = SUM(Item Price * Quantity)
        BigDecimal totalItemPrice = BigDecimal.ZERO;
        for (RequestOrderItemDto orderItem : requestOrder.getItems()) {
            ItemDto item = productService.findItemById(orderItem.getItemId());
            orderItem.setItem(item);
            totalItemPrice = totalItemPrice.add(item.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        
        // 0-D. Calculate Total Payment = Total Item Price + Shipping Fee - Discount
        BigDecimal totalPayment = totalItemPrice.add(shippingFee).subtract(totalDiscount);
        
        // 1. Create Order
        OrderDto order = OrderDto.builder()
                .orderCode(generateOrderCode())
                .memberId(memberDto.getId())
                .status(OrderStatus.CREATED.toString())
                .shippingFee(BigDecimal.valueOf(9000))
                .totalDiscount(totalDiscount)
                .totalPrice(totalItemPrice)
                .totalPayment(totalPayment)
                .build();
        Long orderId = tradeService.createOrder(order);
        
        // 2. Create Order Item
        for (RequestOrderItemDto reqOrderItem : requestOrder.getItems()) {
            ItemDto itemDto = reqOrderItem.getItem();
            OrderItemDto orderItem = OrderItemDto.builder()
                    .orderId(orderId)
                    .itemId(itemDto.getId())
                    .itemName(itemDto.getName())
                    .itemPrice(itemDto.getPrice())
                    .quantity(reqOrderItem.getQuantity())
                    .shopId(itemDto.getShopId())
                    .build();
            tradeService.createOrderItem(orderItem);
        }
        
        // 3. Create Order Delivery
        OrderDeliveryDto orderDelivery = OrderDeliveryDto.builder()
                .orderId(orderId)
                .awbNumber(deliveryDto.getAwbNumber())
                .courierName(deliveryDto.getCourierName())
                .serviceType(deliveryDto.getServiceType())
                // Sender
                .senderName(sender.getName())
                .senderProvince(sender.getProvince())
                .senderCity(sender.getCity())
                .senderDistrict(sender.getDistrict())
                .senderFullAddress(sender.getAddressLine())
                .senderPostalCode(sender.getPostalCode())
                // Receiver
                .receiverName(receiver.getName())
                .receiverProvince(receiver.getProvince())
                .receiverCity(receiver.getCity())
                .receiverDistrict(receiver.getDistrict())
                .receiverFullAddress(receiver.getAddressLine())
                .receiverPostalCode(receiver.getPostalCode())
                .build();
        tradeService.createOrderDelivery(orderDelivery);
        
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("messsage"   , "success");
        maps.put("order_code" , order.getOrderCode());
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(maps), 
                HttpStatus.OK);
    }
}
