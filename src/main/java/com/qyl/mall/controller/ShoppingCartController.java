package com.qyl.mall.controller;

import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.service.ShoppingCartService;
import com.qyl.mall.utils.ResultMessage;
import com.qyl.mall.vo.CartVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
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
    @GetMapping("/user/{userId}")
    public ResultMessage getCartByUserId(@PathVariable Integer userId) {
        List<CartVO> cartVOList = shoppingCartService.getCartByUserId(userId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), cartVOList);
    }

    /**
     * 添加购物车
     * @param productId
     * @param userId
     * @return
     */
    @PostMapping("/add")
    public ResultMessage addCart(Integer productId, Integer userId) {
        CartVO cartVO = shoppingCartService.addCart(productId, userId);
        if (cartVO != null) {
            return ResultMessage.success(ResponseEnum.ADD_CART_SUCCESSFULLY.getCode(), ResponseEnum.ADD_CART_SUCCESSFULLY.getMsg(), cartVO);
        }
        return ResultMessage.success(ResponseEnum.CART_NUM_PLUS_ONE.getCode(), ResponseEnum.CART_NUM_PLUS_ONE.getMsg());
    }

    /**
     * 更新购物车商品数量
     * @param cartId
     * @param userId
     * @param num
     * @return
     */
    @PostMapping("/update/num")
    public ResultMessage UpdateCartNum(Integer cartId, Integer userId, Integer num) {
        shoppingCartService.UpdateCartNum(cartId, userId, num);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    /**
     * 从购物车中移除
     * @param cartId
     * @param userId
     * @return
     */
    @PostMapping("/delete")
    public ResultMessage deleteCart(Integer cartId, Integer userId) {
        shoppingCartService.deleteCart(cartId, userId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }
}
