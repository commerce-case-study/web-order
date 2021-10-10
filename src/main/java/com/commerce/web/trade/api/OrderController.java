package com.commerce.web.trade.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.lib.JsonConverterUtil;
import com.commerce.web.trade.config.annotation.NeedLogin;
import com.commerce.web.trade.dto.MemberDetail;
import com.commerce.web.trade.dto.MemberDto;
import com.commerce.web.trade.dto.OrderDeliveryDto;
import com.commerce.web.trade.dto.OrderDto;
import com.commerce.web.trade.dto.OrderItemDto;
import com.commerce.web.trade.dto.OrderPaymentDto;
import com.commerce.web.trade.dto.DeliveryDto;
import com.commerce.web.trade.dto.ItemDto;
import com.commerce.web.trade.dto.MemberAddressDto;
import com.commerce.web.trade.dto.RequestOrderDto;
import com.commerce.web.trade.dto.RequestOrderItemDto;
import com.commerce.web.trade.dto.RequestPaymentDto;
import com.commerce.web.trade.dto.ShopDto;
import com.commerce.web.trade.enums.OrderStatus;
import com.commerce.web.trade.enums.PaymentStatus;
import com.commerce.web.trade.service.MemberService;
import com.commerce.web.trade.service.ProductService;
import com.commerce.web.trade.service.TradeService;

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
    
    /**
     * Generate Payment Code
     * @return
     */
    private String generatePaymentCode() {
        return "PYM"+System.currentTimeMillis();
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
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createOrder(MemberDetail memberDetail, @RequestBody RequestOrderDto requestOrder) {
        
        List<ItemDto> items = new ArrayList<ItemDto>();
        
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
            items.add(item);
            totalItemPrice = totalItemPrice.add(item.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        
        // 0-D. Calculate Total Payment = Total Item Price + Shipping Fee - Discount
        BigDecimal totalPayment = totalItemPrice.add(shippingFee).subtract(totalItemPrice);
        
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
        for (ItemDto itemDto : items) {
            OrderItemDto orderItem = OrderItemDto.builder()
                    .orderId(orderId)
                    .itemId(itemDto.getId())
                    .itemName(itemDto.getName())
                    .itemPrice(itemDto.getPrice())
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
        maps.put("order_code"    , order.getOrderCode());
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(maps), 
                HttpStatus.OK);
    }
    
    @NeedLogin
    @PostMapping(value = "/payment", consumes = "application/json")
    public ResponseEntity<String> createOrder(MemberDetail memberDetail, @RequestBody RequestPaymentDto requestPayment) {
        
        // 1. Find Order Data
        OrderDto orderDto = tradeService.findOrderByOrderCode(requestPayment.getOrderCode());

        // 4. Create Order Payment
        OrderPaymentDto orderPayment = OrderPaymentDto.builder()
                .orderId(orderDto.getId())
                .paymentCode(generatePaymentCode())
                .status(PaymentStatus.CREATED.toString())
                .amount(orderDto.getTotalPayment())
                .build();
        tradeService.createOrderPayment(orderPayment);
        
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("payment_code"  , orderPayment.getPaymentCode());
        maps.put("payment_url"   , "https://please-pay-your-order.here/payment/"+orderPayment.getPaymentCode());
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(maps), 
                HttpStatus.OK);
    }
}
