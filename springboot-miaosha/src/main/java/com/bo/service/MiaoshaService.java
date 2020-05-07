package com.bo.service;

import com.bo.exception.CMSException;
import com.bo.pojo.MiaoshaOrder;
import com.bo.pojo.MiaoshaUser;
import com.bo.rabbitmq.MQProduct;
import com.bo.rabbitmq.MiaoshaMessage;
import com.bo.redis.MiaoshaGoodsKey;
import com.bo.redis.MiaoshaPathKey;
import com.bo.result.ResultCodeEnum;
import com.bo.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@Transactional
public class MiaoshaService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private MQProduct mqProduct;


    /**
     * 若满足秒杀条件，将消息压入队列，进行秒杀
     */
    public int miaosha(Long goodsId, HttpServletRequest request, MiaoshaUser user,Map<Long,Boolean> isOver) {

        //检查是否有库存
        if (isOver.get(goodsId) == null){
            throw new CMSException(ResultCodeEnum.GOODS_NOT_EXISTS);
        }
        if (isOver.get(goodsId)){
            throw new CMSException(ResultCodeEnum.MIAOSHA_IS_OVER);
        }

        //判断是否已经秒杀过了
        MiaoshaOrder order = orderService.queryMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            throw new CMSException(ResultCodeEnum.GOODS_REPEAT_KILL);
        }

        //对redis的库存进行减一
        Long stock = jedisUtil.decr(MiaoshaGoodsKey.stock, goodsId + "");
        if (stock < 0){//若没有库存了，将对应的商品标记为true，表示秒杀已结束
            isOver.put(goodsId, true);
            throw new CMSException(ResultCodeEnum.MIAOSHA_IS_OVER);
        }


        //入队，进行异步下单
        MiaoshaMessage msg = new MiaoshaMessage();
        msg.setMiaoshaUser(user);
        msg.setGoodsId(goodsId);
        mqProduct.sendMsg(msg);
        return 0;
    }

    /**
     * 生成秒杀接口的地址
     * @param user
     * @param goodsId
     * @return
     */
    public String createMiaoshaPath(MiaoshaUser user, Long goodsId) {
        if (user == null || goodsId <= 0){
            return null;
        }
        //进行一次MD5加密
        String path = MD5Util.MD5(UUIDUtil.getUUID());
        jedisUtil.set(MiaoshaPathKey.path,user.getId()+"_"+goodsId,path,30);
        return path;
    }

    /**
     * 校验秒杀参数是否正确
     */
    public boolean checkMiaoshaPath(MiaoshaUser user, Long goodsId, String param) {
        if (user == null || goodsId <= 0){
            return false;
        }

        String path = jedisUtil.get(MiaoshaPathKey.path, user.getId() + "_" + goodsId);
        return param.equals(path);
    }
}
