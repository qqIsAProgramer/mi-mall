package com.qyl.mall.vo;

import com.qyl.mall.pojo.Order;
import lombok.Data;

/**
 * @Author: qyl
 * @Date: 2020/12/8 14:56
 */
@Data
public class OrderVO extends Order {

    private String productName;

    private String productPicture;
}
