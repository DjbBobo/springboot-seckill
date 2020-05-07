package com.bo.utils;

import com.bo.pojo.MiaoshaUser;
import com.bo.redis.MiaoshaUserKey;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class CookieUtil {
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JsonUtil jsonUtil;

    public final int EXPIREDSECONDS = 3600*2*24;//两天

    /**
     * 将token添加到Cookie中，用于登陆识别
     */
    public void addCookie(MiaoshaUser newUser, String token, HttpServletResponse response) {
        //存储到redis,时效两天
        jedisUtil.set(MiaoshaUserKey.token,token,jsonUtil.toString(newUser),EXPIREDSECONDS);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(EXPIREDSECONDS);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取Cookie中的token令牌
     * @param request
     * @return
     */
    public String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0)
            return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                return cookie.getValue();
            }
        }
        return null;
    }
}
