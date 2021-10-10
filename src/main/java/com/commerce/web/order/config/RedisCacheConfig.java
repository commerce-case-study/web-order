package com.commerce.web.order.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import lombok.RequiredArgsConstructor;

@EnableCaching
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
@Configuration
public class RedisCacheConfig {
    
    final private RedisProperties redisProperties;
    
    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        /*
         * Sentinel Configuration - Development, Staging and Production Profile
         */
        if(redisProperties.getSentinel()!=null) {
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(redisProperties.getSentinel().getMaster());
            redisProperties.getSentinel().getNodes().forEach(node -> {
                sentinelConfig.sentinel(node, redisProperties.getPort());
                sentinelConfig.setPassword(redisProperties.getPassword());
            });
            sentinelConfig.setDatabase(redisProperties.getDatabase());
            sentinelConfig.setPassword(redisProperties.getPassword());
            return new LettuceConnectionFactory(sentinelConfig);
        }
        
        /*
         * Stand-alone Configuration - Default Profile
         */
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        standaloneConfig.setDatabase(redisProperties.getDatabase());
        standaloneConfig.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(standaloneConfig);
        
    }
    
    private RedisCacheConfiguration cacheConfig(Duration duration) {
        RedisSerializationContext.SerializationPair<Object> jsonSerializer = 
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
        
        return RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(duration)
                    .serializeValuesWith(jsonSerializer);
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory lettuceConnectionFactory) {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        return RedisCacheManager
                .builder(lettuceConnectionFactory)
                .withInitialCacheConfigurations(configurationMap)
                .cacheDefaults(cacheConfig(Duration.ofMinutes(4)))
                .build();
    }
    
}

