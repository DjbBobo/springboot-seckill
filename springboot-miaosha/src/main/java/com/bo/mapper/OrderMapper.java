package com.bo.mapper;

import com.bo.pojo.MiaoshaOrder;
import com.bo.pojo.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper {
    void createOrder(OrderInfo orderInfo);

    void createMiaoshaOrder(MiaoshaOrder order);

    MiaoshaOrder queryMiaoshaOrderByUserIdGoodsId(@Param("userId") Long id, @Param("goodsId") Long goodsId);
}
