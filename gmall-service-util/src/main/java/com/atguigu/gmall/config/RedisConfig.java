package com.atguigu.gmall.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host:disabled}")
    private  String host;

    @Value("${spring.redis.port:0}")
    private  int port;

    @Value("${spring.redis.database:0}")
    private  int database;

    @Bean
    public  RedisUtil getRedisUtil(){
        if ("disabled".equals(host)){
            return  null;
        }
        // 就是给连接池进行初始化。
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.initJedisPool(host,port,database);
        return redisUtil;
    }
}
