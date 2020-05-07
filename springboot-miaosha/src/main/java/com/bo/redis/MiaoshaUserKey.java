package com.bo.redis;

/**
 * 秒杀用户前缀
 */
public class MiaoshaUserKey extends BasePrefix {
    public MiaoshaUserKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey("token:");
    public static MiaoshaUserKey userAccess = new MiaoshaUserKey("UserAccess:");
    public static MiaoshaUserKey checkCode = new MiaoshaUserKey("checkCode:");
}
