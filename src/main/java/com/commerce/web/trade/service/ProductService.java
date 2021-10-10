package com.commerce.web.trade.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.web.trade.dto.ItemDto;

@FeignClient(name = ProductService.SERVICE_NAME)
public interface ProductService {

    public static final String SERVICE_NAME = "service-product";
    
    @PostMapping(value = "addItem", consumes = "application/json")
    public Long addProduct(@RequestBody ItemDto product);

    @GetMapping(value = "findItemById/{id}", produces = "application/json")
    public ItemDto findItemById(@PathVariable Long id);
}
