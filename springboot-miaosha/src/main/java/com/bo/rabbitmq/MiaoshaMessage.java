package com.bo.rabbitmq;

import com.bo.pojo.MiaoshaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private Long goodsId;
}
