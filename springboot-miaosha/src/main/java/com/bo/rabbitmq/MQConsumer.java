package com.bo.rabbitmq;

import com.bo.config.MQConfig;
import com.bo.pojo.MiaoshaOrder;
import com.bo.pojo.MiaoshaUser;
import com.bo.service.GoodsService;
import com.bo.service.OrderService;
import com.bo.utils.JsonUtil;
import com.bo.vo.MiaoshaGoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQConsumer {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    /**
     * 监听秒杀消息队列
     * @param msg
     */
    @RabbitListener(queues = MQConfig.MIAOSHAQUEUE)
    public void receiveMsg(String msg) {

        MiaoshaMessage message = jsonUtil.toBean(msg, MiaoshaMessage.class);
        MiaoshaUser user = message.getMiaoshaUser();
        Long goodsId = message.getGoodsId();

        MiaoshaGoodsVo goodsVo = goodsService.queryMiaoshaGoodsByGodosId(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount <= 0){
            return;
        }

        //判断是否已经秒杀过了
        MiaoshaOrder order = orderService.queryMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        //减库存，下订单 写入秒杀订单
        int row = goodsService.reduceStockCount(goodsId);
        //判断是否成功减掉库存
        if (row != 1) {
            return;
        }

        //创建订单
        orderService.createOrder(user, goodsVo);

        logger.info("消费成功："+user.toString());
    }
}
