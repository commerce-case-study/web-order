package com.commerce.web.trade.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.lib.JsonConverterUtil;
import com.commerce.web.trade.config.annotation.NeedLogin;
import com.commerce.web.trade.dto.MemberDetail;
import com.commerce.web.trade.dto.MemberDto;
import com.commerce.web.trade.service.MemberService;

@RestController
@RequestMapping("/api/microsite/member")
public class MemberInfoController {

    Logger logger = LoggerFactory.getLogger(MemberInfoController.class);
    
    @Autowired
    MemberService memberService;
    
    @NeedLogin
    @GetMapping("/info")
    public ResponseEntity<String> info(MemberDetail memberDetail) {
        
        // Gather User Detail based on Member Detail Username
        MemberDto memberDto = memberService.findByUsername(memberDetail.getUserName());
        
        // Show the response
        return new ResponseEntity<String>(
                JsonConverterUtil.convertObjectToJson(memberDto), 
                HttpStatus.OK);
    }
    
    @NeedLogin
    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<String> update(MemberDetail memberDetail, @RequestBody MemberDto member) {
        
        // Gather User Detail based on Member Detail Username
        MemberDto memberDto = memberService.findByUsername(memberDetail.getUserName());
        
        // Prepare and Update Parameter
        member.setId(memberDto.getId());
        
        if(memberService.updateMember(member) > 0) {
        	// Show the response
        	return new ResponseEntity<String>(
                    "{\"msg\":\"Success Update Member\"}", 
                    HttpStatus.OK);
        } else {
        	// Show the response
        	return new ResponseEntity<String>(
                    "{\"msg\":\"Failed Update Member\"}", 
                    HttpStatus.OK);
        }
    }
}
