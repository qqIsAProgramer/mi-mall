package com.qyl.mall.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: qyl
 * @Date: 2020/12/5 9:39
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum {

    SAVE_USER_REUSE(0, "用户已存在"),
    USER_NOT_FOUND(0, "用户不存在"),

    CATEGORY_NOT_FOUND(0, "分类查询为空"),

    PRODUCT_NOT_FOUND(0, "商品查询为空"),

    PICTURE_NOT_FOUND(0, "商品图片为空"),

    CART_NUM_UPPER(0, "该商品购物车达到上限"),
    ;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;
}
