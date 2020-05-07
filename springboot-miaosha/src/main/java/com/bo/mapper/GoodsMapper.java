package com.bo.mapper;

import com.bo.pojo.Goods;
import com.bo.vo.MiaoshaGoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsMapper {

    List<Goods> queryGoodsList();

    List<MiaoshaGoodsVo> queryMiaoshaGoodsList();

    MiaoshaGoodsVo queryMiaoshaGoodsByGodosId(@Param("goodsId") Long goodsId);

    int reduceStockCount(@Param("goodsId") Long id);
}
