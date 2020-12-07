package com.qyl.mall.service;

import com.qyl.mall.vo.CartVO;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/7 8:31
 */
public interface ShoppingCartService {

    /**
     * 通过用户ID获取对应购物车中的所有商品
     * @param userId
     * @return
     */
    List<CartVO> getCartByUserId(Integer userId);

    /**
     * 添加购物车
     * @param productId
     * @param userId
     * @return
     */
    CartVO addCart(Integer productId, Integer userId);

    /**
     * 更新购物车商品的数量
     * @param cartId
     * @param userId
     * @param num
     */
    void UpdateCartNum(Integer cartId, Integer userId, Integer num);

    /**
     * 从购物车中移除
     * @param cartId
     * @param userId
     */
    void deleteCart(Integer cartId, Integer userId);
}
