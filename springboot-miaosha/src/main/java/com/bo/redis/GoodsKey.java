package com.bo.redis;

public class GoodsKey extends BasePrefix{
    public GoodsKey(String prefix) {
        super(prefix);
    }

    public static GoodsKey goodsList = new GoodsKey("GoodsList:");
}
