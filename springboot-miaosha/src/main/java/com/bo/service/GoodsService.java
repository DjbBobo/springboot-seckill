package com.bo.service;

import com.bo.mapper.GoodsMapper;
import com.bo.pojo.Goods;
import com.bo.redis.GoodsKey;
import com.bo.redis.MiaoshaGoodsKey;
import com.bo.utils.JedisUtil;
import com.bo.utils.JsonUtil;
import com.bo.vo.MiaoshaGoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JsonUtil jsonUtil;

    /**
     * 查询商品列表
     * @return
     */
    public List<Goods> queryGoodsList() {
        String goodsListStr = jedisUtil.get(GoodsKey.goodsList, "GoodsList");
        List<Goods> list = null;
        if (!StringUtils.isEmpty(goodsListStr)){
            return jsonUtil.toList(goodsListStr,Goods.class);
        }
        list = goodsMapper.queryGoodsList();
        jedisUtil.set(GoodsKey.goodsList, "GoodsList", jsonUtil.toString(list));
        return list;
    }

    /**
     * 查询秒杀商品列表
     * @return
     */
    public List<MiaoshaGoodsVo> queryMiaoshaGoodsList() {
        String miaoshaGoodsListStr = jedisUtil.get(MiaoshaGoodsKey.goodsList, "MiaoshaGoodsList");
        List<MiaoshaGoodsVo> list = null;
        if (!StringUtils.isEmpty(miaoshaGoodsListStr)){
           return jsonUtil.toList(miaoshaGoodsListStr, MiaoshaGoodsVo.class);
        }
        list = goodsMapper.queryMiaoshaGoodsList();
        jedisUtil.set(MiaoshaGoodsKey.goodsList,"MiaoshaGoodsList",jsonUtil.toString(list));
        return list;
    }

    /**
     * 根据商品Id查询商品信息
     * @param goodsId
     * @return
     */
    public MiaoshaGoodsVo queryMiaoshaGoodsByGodosId(Long goodsId) {
        //取缓存
        String miaoshaGoodsStr = jedisUtil.get(MiaoshaGoodsKey.goods, goodsId + "");
        MiaoshaGoodsVo goods = null;
        if (!StringUtils.isEmpty(miaoshaGoodsStr)){
            goods = jsonUtil.toBean(miaoshaGoodsStr, MiaoshaGoodsVo.class);
            return goods;
        }
        //查数据库
        goods = goodsMapper.queryMiaoshaGoodsByGodosId(goodsId);
        if (goods != null){
            //存进缓存
            jedisUtil.set(MiaoshaGoodsKey.goods,goodsId+"",jsonUtil.toString(goods));
        }
        return goods;
    }

    /**
     * 减库存
     * @param id
     * @return
     */
    public int reduceStockCount(Long id) {
        return goodsMapper.reduceStockCount(id);
    }
}
