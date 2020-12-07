package com.qyl.mall.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: qyl
 * @Date: 2020/12/6 18:16
 */
@Data
public class ProductPicture {

    /**
     * 商品图片ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Integer id;

    /**
     * 对应商品ID
     */
    private Integer productId;

    /**
     * 商品图片
     */
    private String productPicture;

    /**
     * 图片简介
     */
    private String intro;
}
