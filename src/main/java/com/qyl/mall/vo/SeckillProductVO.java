package com.qyl.mall.vo;

import com.qyl.mall.pojo.SeckillProduct;
import lombok.Data;

/**
 * @Author: qyl
 * @Date: 2020/12/7 14:53
 */
@Data
public class SeckillProductVO extends SeckillProduct {

    private String productName;

    private Double productPrice;

    private String productPicture;

    private Long startTime;

    private Long endTime;
}
