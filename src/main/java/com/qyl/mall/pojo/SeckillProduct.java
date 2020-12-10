package com.qyl.mall.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: qyl
 * @Date: 2020/12/7 14:46
 */
@Data
public class SeckillProduct {

    /**
     * 秒杀ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Integer seckillId;

    /**
     * 秒杀商品ID
     */
    private Integer productId;

    /**
     * 秒杀价格
     */
    private Double seckillPrice;

    /**
     * 秒杀库存
     */
    private Integer seckillStock;

    /**
     * 秒杀对应的时间ID
     */
    private Integer timeId;
}
