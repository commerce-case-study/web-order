package com.commerce.web.order.config.resolver;

import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.commerce.web.order.dto.MemberDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemberDetailArgumentResolver implements HandlerMethodArgumentResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getType() == MemberDetail.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return getMemberDetail(webRequest);
    }
    
    /**
     * Get Decoded JWT with Base64 Encoded
     * @param request
     * @return
     */
    private String getContent(NativeWebRequest request) {
        String decodedJwt = request.getHeader("X-jwt-sub");
        if(StringUtils.isBlank(decodedJwt)) {
            return "";
        }
        try {
            return new String(Base64.getDecoder().decode(decodedJwt));
        } catch (Exception e) {
            log.error("Error", e);
        }
        return "";
    }
    
    /**
     * Get Member Detail
     * @param request
     * @return
     */
    public MemberDetail getMemberDetail(NativeWebRequest request){
        String content = getContent(request);
        if(StringUtils.isBlank(content)) {
            return null;
        }
        
        MemberDetail memberDetail = null;
        try {
            memberDetail = new ObjectMapper().readValue(content, MemberDetail.class);
        }catch (Exception e) {
            log.error("Error", e);
        }
        return memberDetail;
    }

}
