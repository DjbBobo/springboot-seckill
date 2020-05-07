package com.bo.pojo;

import lombok.Data;

@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
    private String goodsImg;
}
