package com.qyl.mall.vo;

import lombok.Data;

/**
 * @Author: qyl
 * @Date: 2020/12/6 23:19
 */
@Data
public class CartVO {

    private Integer id;

    private Integer productId;

    private String productName;

    private String productPicture;

    private Double price;

    private Integer num;

    private Integer maxNum;

    private Boolean check;
}
