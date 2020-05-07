package com.bo;

import com.bo.utils.CookieUtil;
import com.bo.utils.IdWorker;
import com.bo.utils.JedisUtil;
import com.bo.utils.JsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.locks.Lock;

@EnableAsync
@SpringBootApplication
public class SpringbootMiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMiaoshaApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker(){
        return new IdWorker(1, 1);
    }

    @Bean
    public JedisUtil jedisUtil(){
        return new JedisUtil();
    }

    @Bean
    public JsonUtil jsonUtil(){
        return new JsonUtil();
    }

    @Bean
    public CookieUtil cookieUtil(){
        return new CookieUtil();
    }

}
