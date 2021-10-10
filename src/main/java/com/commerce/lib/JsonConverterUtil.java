package com.commerce.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverterUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonConverterUtil.class);
    
    public static String convertObjectToJson(Object o){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(o);
        } catch (Exception e) {
            logger.error("Error converting Item into JSON", e);
            return "{}";
        }
    }
}

