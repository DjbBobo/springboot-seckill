package com.bo.service;

import com.bo.mapper.OrderMapper;
import com.bo.pojo.MiaoshaOrder;
import com.bo.pojo.MiaoshaUser;
import com.bo.pojo.OrderInfo;
import com.bo.redis.MiaoshaOrderKey;
import com.bo.utils.IdWorker;
import com.bo.utils.JedisUtil;
import com.bo.utils.JsonUtil;
import com.bo.vo.MiaoshaGoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JsonUtil jsonUtil;

    /**
     * 查询秒杀订单
     * @param id
     * @param goodsId
     * @return
     */
    public MiaoshaOrder queryMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId) {
        //查询缓存
        String orderStr = jedisUtil.get(MiaoshaOrderKey.order, id + "_" + goodsId);
        if (!StringUtils.isEmpty(orderStr)){
            return jsonUtil.toBean(orderStr, MiaoshaOrder.class);
        }

        return null;
    }

    /**
     * 创建订单
     * @param user
     * @param goods
     */
    public void createOrder(MiaoshaUser user,MiaoshaGoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        Long orderId = Long.valueOf(idWorker.nextId());
        orderInfo.setId(orderId);
        orderInfo.setUserId(user.getId());
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setCreateDate(new Date());
        orderMapper.createOrder(orderInfo);
        MiaoshaOrder order = new MiaoshaOrder();
        order.setOrderId(orderId);
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        orderMapper.createMiaoshaOrder(order);
        //秒杀订单存进缓存
        jedisUtil.set(MiaoshaOrderKey.order,user.getId()+"_"+goods.getId(),jsonUtil.toString(order));
    }


}
