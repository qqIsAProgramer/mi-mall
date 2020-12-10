package com.qyl.mall.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: qyl
 * @Date: 2020/12/8 14:50
 */
@Data
public class Order {

    /**
     * 订单在数据库中的ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Integer id;

    /**
     * 通过IdWorker得到的订单ID
     */
    private String orderId;

    /**
     * 订单的用户ID
     */
    private Integer userId;

    /**
     * 对应商品的ID
     */
    private Integer productId;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 商品总价
     */
    private Double productPrice;

    /**
     * 商品时间
     */
    private Long orderTime;
}
