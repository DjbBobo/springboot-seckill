package com.bo.redis;

/**
 * 秒杀订单前缀
 */
public class MiaoshaOrderKey extends BasePrefix{
    public MiaoshaOrderKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaOrderKey order = new MiaoshaOrderKey("MiaoshaOrder:");
}
