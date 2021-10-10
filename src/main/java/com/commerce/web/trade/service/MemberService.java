package com.commerce.web.trade.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.web.trade.dto.MemberAddressDto;
import com.commerce.web.trade.dto.MemberDto;

@FeignClient(name = MemberService.SERVICE_NAME)
public interface MemberService {
    
    public static final String SERVICE_NAME = "service-member";
    
    @GetMapping(value = "findByEmailOrUsername/{email}/{username}", produces = "application/json")
    public MemberDto findByEmailOrUsername(@PathVariable("email") String email, @PathVariable("username") String username);

    @GetMapping(value = "findByEmailOrUsernameOrPhone/{email}/{username}/{phone}", produces = "application/json")
    public MemberDto findByEmailOrUsernameOrPhone(@PathVariable("email") String email, @PathVariable("username") String username, @PathVariable("phone") String phone);

    @GetMapping(value = "findByUsername/{username}", produces = "application/json")
    public MemberDto findByUsername(@PathVariable("username") String username);
    
    @PostMapping(value = "registerMember", consumes = "application/json")
    public Long registerMember(@RequestBody MemberDto member);
    
    @PostMapping(value = "updateMember", consumes = "application/json")
    public Long updateMember(@RequestBody MemberDto member);
    
    @GetMapping(value = "findMemberAddressByMemberId/{memberId}", produces = "application/json")
    public List<MemberAddressDto> findMemberAddressByMemberId(@PathVariable("memberId") Long memberId);
    
    @GetMapping(value = "findMemberAddressById/{id}", produces = "application/json")
    public MemberAddressDto findMemberAddressById(@PathVariable("id") Long id);
    
    @PostMapping(value = "addMemberAddress", consumes = "application/json")
    public Long addMemberAddress(@RequestBody MemberAddressDto memberAddress);
    
    @PostMapping(value = "updateMemberAddress", consumes = "application/json")
    public Long updateMemberAddress(@RequestBody MemberAddressDto memberAddress);
}

