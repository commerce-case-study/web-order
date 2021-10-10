package com.commerce.web.order.dto;

import lombok.Data;

@Data
public class RequestOrderItemDto {

	private Long itemId;
	
	private Integer quantity;
	
	private ItemDto item;
}
