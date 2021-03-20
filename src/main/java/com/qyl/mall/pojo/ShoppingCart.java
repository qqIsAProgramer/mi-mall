package com.qyl.mall.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: qyl
 * @Date: 2020/12/6 23:07
 */
@Data
public class ShoppingCart {

    /**
     * 购物车ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 购物车商品的ID
     */
    private Integer productId;

    /**
     * 数量
     */
    private Integer num;
}
