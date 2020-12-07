package com.qyl.mall.service.impl;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.ProductMapper;
import com.qyl.mall.mapper.ShoppingCartMapper;
import com.qyl.mall.pojo.Product;
import com.qyl.mall.pojo.ShoppingCart;
import com.qyl.mall.service.ShoppingCartService;
import com.qyl.mall.vo.CartVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/7 8:38
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private ProductMapper productMapper;

    @Override
    public List<CartVO> getCartByUserId(Integer userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.select(shoppingCart);
        List<CartVO> cartVOList = new ArrayList<>();
        for (ShoppingCart c : shoppingCartList) {
            cartVOList.add(getCartVO(c));
        }
        return cartVOList;
    }

    @Override
    @Transactional
    public CartVO addCart(Integer productId, Integer userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setProductId(productId);
        shoppingCart.setUserId(userId);

        ShoppingCart cart = shoppingCartMapper.selectOne(shoppingCart);
        // 查看数据库中是否已存在，存在则数量+1
        if (cart != null) {
            // 判断商品是否已达上限数量
            if (cart.getNum() >= 5) {
                throw new MyException(ExceptionEnum.CART_NUM_UPPER);
            }
            cart.setNum(cart.getNum() + 1);
            shoppingCartMapper.updateByPrimaryKey(cart);
            return null;
        } else {
            shoppingCart.setNum(1);
            shoppingCartMapper.insert(shoppingCart);
            return getCartVO(shoppingCart);
        }
    }

    /**
     * 封装类
     * @param cart
     * @return
     */
    private CartVO getCartVO(ShoppingCart cart) {
        // 获取商品，用于封装
        Product product = productMapper.selectByPrimaryKey(cart.getProductId());

        // 返回购物车详情
        CartVO cartVO = new CartVO();
        cartVO.setId(cart.getId());
        cartVO.setProductId(cart.getProductId());
        cartVO.setProductName(product.getProductName());
        cartVO.setProductImg(product.getProductPicture());
        cartVO.setPrice(product.getProductSellingPrice());
        cartVO.setNum(cart.getNum());
        // 这里默认设置为5，后面再动态修改
        cartVO.setMaxNum(5);
        cartVO.setCheck(false);

        return cartVO;
    }

    @Override
    public void UpdateCartNum(Integer cartId, Integer userId, Integer num) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);
        cart.setUserId(userId);
        cart.setNum(num);
        shoppingCartMapper.updateByPrimaryKey(cart);
    }

    @Override
    public void deleteCart(Integer cartId, Integer userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);
        cart.setUserId(userId);
        shoppingCartMapper.delete(cart);
    }
}
