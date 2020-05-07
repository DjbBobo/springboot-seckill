package com.bo.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * MD5工具类
 */
public class MD5Util {
    /**
     * MD5加密
     * @param src
     * @return
     */
    public static String MD5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     * 密码加盐加密
     * 盐的前5位 + 密码 + 盐的后5位
     * @param password
     * @param salt
     * @return
     */
    public static String encryptPassword(String password,String salt){
        return ""+salt.substring(0,5) + password + salt.substring(5);
    }

}
