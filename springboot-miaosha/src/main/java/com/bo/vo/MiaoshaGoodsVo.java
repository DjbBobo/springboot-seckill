package com.bo.vo;

import lombok.Data;

import java.util.Date;

/**
 * MiaoshaGoods & Goods
 */
@Data
public class MiaoshaGoodsVo {
    private Long id;
    private String goodsName;
    private String goodsDetail;
    private String goodsImg;
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
