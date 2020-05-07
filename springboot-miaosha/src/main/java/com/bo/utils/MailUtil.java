package com.bo.utils;

import com.bo.redis.MiaoshaUserKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String originMail;


    public void sendEmail(String email,String code) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("秒杀系统注册账号测试");
        mailMessage.setText("验证码:"+code);
        mailMessage.setFrom(originMail); // 发件人
        mailMessage.setTo(email); //收件人
        mailSender.send(mailMessage);
    }
}
