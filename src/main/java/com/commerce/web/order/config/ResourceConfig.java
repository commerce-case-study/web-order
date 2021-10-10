package com.commerce.web.order.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.commerce.web.order.config.interceptor.NeedLoginInterceptor;
import com.commerce.web.order.config.resolver.MemberDetailArgumentResolver;

@Component
public class ResourceConfig implements WebMvcConfigurer {
    
    @Autowired
    private NeedLoginInterceptor needLoginInterceptor;
    
    @Autowired
    private MemberDetailArgumentResolver memberDetailArgumentResolver;
    
    /**
     * Register Interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(needLoginInterceptor);
    }
    
    /**
     * Register Parameter Resolver
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberDetailArgumentResolver);
    }

}
