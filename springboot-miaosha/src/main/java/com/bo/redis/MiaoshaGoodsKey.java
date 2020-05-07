package com.bo.redis;

/**
 * 秒杀商品前缀
 */
public class MiaoshaGoodsKey extends BasePrefix {
    public MiaoshaGoodsKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaGoodsKey goods = new MiaoshaGoodsKey("MiaoshaGoods:");
    public static MiaoshaGoodsKey goodsList = new MiaoshaGoodsKey("MiaoshaGoodsList:");
    public static MiaoshaGoodsKey stock = new MiaoshaGoodsKey("MiaoshaGoodsStock:");
}
