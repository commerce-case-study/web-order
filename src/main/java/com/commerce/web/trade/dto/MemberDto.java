package com.commerce.web.trade.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class MemberDto {

    private Long id;
    
    private String username;
    
    private String email;
    
    private String password;
    
    private String type;
    
    private String image; 
    
    private String status;
    
    private String phone;
    
}
