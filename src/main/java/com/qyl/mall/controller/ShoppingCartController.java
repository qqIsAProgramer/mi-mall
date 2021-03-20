package com.qyl.mall.controller;

import com.qyl.mall.service.ShoppingCartService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.vo.CartVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车相关接口
 * @Author: qyl
 * @Date: 2020/12/7 9:13
 */
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 通过用户ID获取对应购物车中的所有商品
     * @param userId
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<List<CartVO>> getCartByUserId(Integer userId) {
        return shoppingCartService.getCartByUserId(userId);
    }

    /**
     * 添加购物车
     * @param productId
     * @param userId
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addCart(Integer productId, Integer userId) {
        return shoppingCartService.addCart(productId, userId);
    }

    /**
     * 更新购物车商品数量
     * @param cartId
     * @param userId
     * @param num
     * @return
     */
    @PostMapping("/update/num")
    public ResponseEntity<Void> updateCartNum(Integer cartId, Integer userId, Integer num) {
        return shoppingCartService.updateCartNum(cartId, userId, num);
    }

    /**
     * 从购物车中移除
     * @param cartId
     * @param userId
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteCart(Integer cartId, Integer userId) {
        return shoppingCartService.deleteCart(cartId, userId);
    }
}
