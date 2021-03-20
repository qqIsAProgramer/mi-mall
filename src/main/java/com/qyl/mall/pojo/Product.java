package com.qyl.mall.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: qyl
 * @Date: 2020/12/6 12:09
 */
@Data
public class Product {

    /**
     * 商品ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 所在的分类ID
     */
    private Integer categoryId;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 商品图片
     */
    private String productPicture;

    /**
     * 商品价格
     */
    private Double productPrice;

    /**
     * 商品促销价
     */
    private Double productSellingPrice;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 商品销售数
     */
    private Integer productSales;

    /**
     * 商品简介
     */
    private String productIntro;
}
