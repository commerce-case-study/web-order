package com.commerce.web.order.config.interceptor;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.commerce.web.order.config.annotation.NeedLogin;
import com.commerce.web.order.config.exception.MemberUnauthorizedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NeedLoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            NeedLogin needLogin = ((HandlerMethod) handler).getMethodAnnotation(NeedLogin.class);
            if(null != needLogin) {
                if("".equals(getContent(request))) {
                    throw new MemberUnauthorizedException();
                }
            }
        }
        return true;
    }
    
    /**
     * Get Decoded JWT with Base64 Encoded
     * @param request
     * @return
     */
    private String getContent(HttpServletRequest request) {
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
}
