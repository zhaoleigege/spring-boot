package com.busekylin.springbootcluster.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@EnableRedisHttpSession
public class RedisSessionConfig {
    /**
     * 设置按照json格式存储session中的内容
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * 不想把session放在cookie中可以采用这种方式把session放在header中
     * @return
     */
//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return new HeaderHttpSessionIdResolver("redisID");
//    }
}
