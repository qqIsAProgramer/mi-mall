package com.qyl.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: qyl
 * @Date: 2020/12/5 10:46
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(1, "成功"),
    FAIL(0, "失败"),

    // 用户模块错误
    TOKEN_EXPIRED(1001, "账号已过期，请重新登陆"),
    USER_EXIST(1002, "用户已存在"),
    USER_NOT_FOUND(1003, "用户不存在"),

    // 分类模块错误
    CATEGORY_NOT_FOUND(2001, "分类查询为空"),

    // 商品模块错误
    PRODUCT_NOT_FOUND(3001, "商品查询为空"),
    PICTURE_NOT_FOUND(3002, "商品图片为空"),
    CART_NUM_UPPER(3003, "该商品购物车达到上限"),

    // 秒杀模块错误
    SECKILL_NOT_FOUND(4001, "尚无秒杀商品"),
    SECKILL_IS_OVER(4002, "秒杀商品售罄"),
    SECKILL_NOT_START(4003, "秒杀尚未开始"),
    SECKILL_IS_REUSE(4004, "重复秒杀"),

    // 订单模块错误
    ORDER_NOT_FOUND(5001, "订单查询为空"),
    ;

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;
}
