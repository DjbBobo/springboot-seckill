package com.bo.controller;

import com.bo.pojo.Goods;
import com.bo.result.R;
import com.bo.service.GoodsService;
import com.bo.vo.MiaoshaGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 获取商品列表
     * @return
     */
    @RequestMapping(value = "/goodsList",method = RequestMethod.GET)
    public R goodsList(){
        List<Goods> goodsList =  goodsService.queryGoodsList();
        return R.ok().data("goodsList",goodsList).message("商品列表");
    }

    /**
     * 获取秒杀商品列表
     * @return
     */
    @RequestMapping(value = "/miaoshaGoodsList",method = RequestMethod.GET)
    public R miaoshaGoodsList(){
        List<MiaoshaGoodsVo> miaoshaGoodsList =  goodsService.queryMiaoshaGoodsList();
        return R.ok().data("miaoshaGoodsList",miaoshaGoodsList).message("秒杀商品列表");
    }



}
