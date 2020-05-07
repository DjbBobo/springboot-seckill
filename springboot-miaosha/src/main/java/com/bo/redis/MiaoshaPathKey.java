package com.bo.redis;

/**
 * 秒杀路径前缀
 */
public class MiaoshaPathKey extends BasePrefix{
    public MiaoshaPathKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaPathKey path = new MiaoshaPathKey("MiaoshaPath:");
}
