package com.bo.service;

import com.bo.exception.CMSException;
import com.bo.mapper.MiaoshaUserMapper;
import com.bo.pojo.MiaoshaUser;
import com.bo.redis.MiaoshaUserKey;
import com.bo.result.ResultCodeEnum;
import com.bo.utils.*;
import com.bo.vo.MiaoshaUserRegisterVo;
import com.bo.vo.MiaoshaUserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@Transactional
public class MiaoshaUserService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MiaoshaUserMapper miaoshaUserMapper;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * 添加用户
     */
    public void addUser(MiaoshaUserRegisterVo user, String checkCode) {
        //查询邮箱是否已注册
        MiaoshaUser tempUser = miaoshaUserMapper.queryUserByEmail(user.getEmail());
        if (tempUser != null) {//用户已存在
            throw new CMSException(ResultCodeEnum.USER_EXISTS);
        }
        //校验验证码
        String code = jedisUtil.get(MiaoshaUserKey.checkCode, user.getEmail());
        if (!checkCode.equals(code)){
            throw new CMSException(ResultCodeEnum.CHECKCODE_ERROR);
        }
        //随机盐+密码加密
        String salt = UUIDUtil.getUUID().substring(0, 10);
        String dbPassword = MD5Util.encryptPassword(user.getPassword(), salt);

        MiaoshaUser miaoshaUser = new MiaoshaUser(null,user.getEmail(),user.getUsername(),dbPassword,salt,new Date());
        miaoshaUserMapper.addUser(miaoshaUser);
        logger.info("注册成功:"+miaoshaUser.toString());
    }

    /**
     * 登陆操作
     */
    public String login(MiaoshaUserVo user, HttpServletResponse response) {
        //查询邮箱是否存在
        MiaoshaUser newUser = miaoshaUserMapper.queryUserByEmail(user.getEmail());
        if (newUser == null) {
            throw new CMSException(ResultCodeEnum.USER_NOT_EXISTS);
        }
        //校验密码
        String pwd = MD5Util.encryptPassword(user.getPassword(), newUser.getSalt());
        if (!newUser.getPassword().equals(pwd)) {
            throw new CMSException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }

        //生产Cookie
        String token = UUIDUtil.getUUID();
        cookieUtil.addCookie(newUser, token, response);
        logger.info("添加Cookie成功");
        return token;
    }

    /**
     * 通过token获取用户信息
     */
    public MiaoshaUser getUserByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String userStr = jedisUtil.get(MiaoshaUserKey.token, token);
        if (userStr == null || userStr.length() <= 0){
            throw new CMSException(ResultCodeEnum.USER_NO_LOGIN);
        }
        MiaoshaUser user = jsonUtil.toBean(userStr, MiaoshaUser.class);
        if (user == null) {
            throw new CMSException(ResultCodeEnum.USER_NO_LOGIN);
        }
        //延长有效期
        cookieUtil.addCookie(user, token, response);
        return user;
    }

    /**
     * 异步发送邮件
     * @param email
     */
    @Async
    public void sendEmail(String email) {
        //6位随机验证码
        String code = CodeUtil.getCheckCode(6);
        jedisUtil.set(MiaoshaUserKey.checkCode, email, code, 180);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("秒杀系统注册账号测试");
        mailMessage.setText("验证码:"+code);
        mailMessage.setFrom("xxxxxx@qq.com"); // 发件人
        mailMessage.setTo(email); //收件人
        mailSender.send(mailMessage);
        logger.info("Send Email Success");
    }
}
