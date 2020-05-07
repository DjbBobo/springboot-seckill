package com.bo.controller;

import com.bo.rabbitmq.MQProduct;
import com.bo.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private MQProduct mqProduct;

    @RequestMapping("/mq")
    public R test(){
        return R.ok().message("发送成功");
    }
}
