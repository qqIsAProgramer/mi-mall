package com.qyl.mall.Enums;

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

    TOKEN_EXPIRED(0, "账号已过期，请重新登陆"),

    ADD_CART_SUCCESSFULLY(1, "成功添加购物车"),
    CART_NUM_PLUS_ONE(1, "购物车数量+1"),
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
