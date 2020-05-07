package com.bo.controller;

import com.bo.pojo.MiaoshaUser;
import com.bo.redis.*;
import com.bo.result.R;
import com.bo.result.ResultCodeEnum;
import com.bo.service.GoodsService;
import com.bo.service.MiaoshaService;
import com.bo.utils.JedisUtil;
import com.bo.vo.MiaoshaGoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private GoodsService goodsService;

    /**
     * 初始化 key:goodsId value:false
     * 当redis库存为0时，将value设置为true，表示已卖光，直接返回
     */
    private Map<Long, Boolean> isOver = new HashMap<>();

    /**
     * 库存预热，并用HashMap内存标记商品是否卖完
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<MiaoshaGoodsVo> list = goodsService.queryMiaoshaGoodsList();
        for (MiaoshaGoodsVo vo : list) {
            jedisUtil.set(MiaoshaGoodsKey.stock, vo.getId() + "", vo.getStockCount()+"");
            isOver.put(vo.getId(),false);
        }
    }

    /**
     * 获取隐藏的秒杀接口地址
     */
    @RequestMapping(value = "/getMiaoshaPath",method = RequestMethod.GET)
    public R getMiaoshaPath(@RequestParam(value = "id",required = true)Long goodsId,
                            HttpServletRequest request,
                            MiaoshaUser user){
        if (user == null){
            return R.setResult(ResultCodeEnum.HTTP_CLIENT_ERROR);
        }

        //接口限流，一个用户5s内最多访问3次
        String uri = request.getRequestURI();
        String count = jedisUtil.get(MiaoshaUserKey.userAccess, uri + "_" + user.getId());
        if (count == null){
            jedisUtil.set(MiaoshaUserKey.userAccess, uri + "_" + user.getId(),"1",5);
        }else if(Integer.valueOf(count) < 3){
            jedisUtil.incr(MiaoshaUserKey.userAccess, uri + "_" + user.getId());
        }else{
            return R.setResult(ResultCodeEnum.ACCESS_COUNT_ERROR);
        }

        String path = miaoshaService.createMiaoshaPath(user,goodsId);
        return R.ok().data("pathParam",path);
    }

    /**
     * 秒杀接口，需传入前面生成的参数
     * @param goodsId
     * @param request
     * @param user
     * @param param
     * @return
     */
    @RequestMapping(value = "/do_miaosha/{param}",method = RequestMethod.POST)
    public R miaosha(@RequestParam(value = "id",required = true)Long goodsId,
                     HttpServletRequest request,
                     MiaoshaUser user,
                     @PathVariable("param")String param){

        if (user == null){
            return R.setResult(ResultCodeEnum.HTTP_CLIENT_ERROR);
        }

        boolean isTrue = miaoshaService.checkMiaoshaPath(user,goodsId,param);
        if (!isTrue){
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        int status = miaoshaService.miaosha(goodsId,request,user,isOver);
        if (status == 0){
            return R.ok().data("status",0).message("排队中");//0：排队中
        }
        return R.error();
    }


    /**
     * 重置redis的值
     * @return
     */
    @RequestMapping(value = "/reset",method = RequestMethod.POST)
    public R reset(){
        List<MiaoshaGoodsVo> goodsVos = goodsService.queryMiaoshaGoodsList();
        for (MiaoshaGoodsVo vo : goodsVos) {
            jedisUtil.set(MiaoshaGoodsKey.stock, vo.getId() + "", vo.getStockCount()+"");
            isOver.put(vo.getId(),true);
            jedisUtil.delBatchByPrefix(MiaoshaOrderKey.order);
            jedisUtil.del(MiaoshaGoodsKey.goods, vo.getId()+"");
            jedisUtil.del(MiaoshaGoodsKey.goodsList, "MiaoshaGoodsList");
        }
        return R.ok();
    }

}
