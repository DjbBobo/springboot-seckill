package com.bo.controller;

import com.bo.pojo.MiaoshaUser;
import com.bo.redis.MiaoshaUserKey;
import com.bo.result.R;
import com.bo.service.MiaoshaUserService;
import com.bo.utils.CodeUtil;
import com.bo.utils.JedisUtil;
import com.bo.vo.MiaoshaUserRegisterVo;
import com.bo.vo.MiaoshaUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.SupportedOptions;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 用户模块
 */
@RestController
@RequestMapping("/user")
public class MiaoshaUserController {
    @Autowired
    private MiaoshaUserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 注册功能
     * @param user 注册的用户信息，封装成MiaoshaUser对象
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public R registerUser(@Validated MiaoshaUserRegisterVo user, @RequestParam(value = "checkCode",required = true)String checkCode){
        userService.addUser(user,checkCode);
        return R.ok().message("注册成功");
    }

    /**
     * 异步发送邮件
     * @param email
     * @return
     */
    @RequestMapping(value = "/sendEmail",method = RequestMethod.POST)
    public R sendEmail(@RequestParam(value = "email",required = true)String email){
        userService.sendEmail(email);
        return R.ok().message("发送成功");
    }


    /**
     * 登陆功能
     * @param user  登陆的用户信息，使用JSR303校验
     * @param response
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public R loginUser(@Validated MiaoshaUserVo user, HttpServletResponse response){
        String token = userService.login(user, response);
        return R.ok().data("token",token).message("登陆成功");
    }



}
