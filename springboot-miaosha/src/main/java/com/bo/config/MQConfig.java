package com.bo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@Configuration
public class MQConfig {
    public static final String MIAOSHAQUEUE = "miaoshaQueue";

    @Bean
    public Queue queue(){
        return new Queue(MIAOSHAQUEUE,true);
    }


}
