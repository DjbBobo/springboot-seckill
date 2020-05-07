package com.bo.rabbitmq;

import com.bo.config.MQConfig;
import com.bo.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQProduct {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private JsonUtil jsonUtil;

    public void sendMsg(MiaoshaMessage message){
        String msg = jsonUtil.toString(message);
        logger.info("发送message:"+msg);
        rabbitTemplate.convertAndSend(MQConfig.MIAOSHAQUEUE,msg);
    }
}
