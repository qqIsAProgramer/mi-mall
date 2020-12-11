package com.qyl.mall.service.impl;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.OrderMapper;
import com.qyl.mall.mapper.ProductMapper;
import com.qyl.mall.mapper.ShoppingCartMapper;
import com.qyl.mall.pojo.Order;
import com.qyl.mall.pojo.Product;
import com.qyl.mall.pojo.ShoppingCart;
import com.qyl.mall.service.OrderService;
import com.qyl.mall.utils.IdWorker;
import com.qyl.mall.vo.CartVO;
import com.qyl.mall.vo.OrderVO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: qyl
 * @Date: 2020/12/11 9:05
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IdWorker idWorker;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public void addOrder(List<CartVO> cartVOList, Integer userId) {
        // 先添加订单
        String orderId = idWorker.nextId() + "";    // 订单ID
        long time = new Date().getTime();   // 订单生成时间
        for (CartVO cartVO : cartVOList) {
            Order order = new Order();
            order.setOrderId(orderId);
            order.setOrderTime(time);
            order.setProductId(cartVO.getProductId());
            order.setProductNum(cartVO.getNum());
            order.setProductPrice(cartVO.getPrice());
            order.setUserId(userId);

            orderMapper.insert(order);
            // 减去商品库存，记录卖出数量
            // TODO : 此处会产生多线程问题，即不同用户同时对这个商品操作，此时会导致数量不一致问题
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            product.setProductNum(product.getProductNum() - cartVO.getNum());
            product.setProductSales(product.getProductSales() + cartVO.getNum());
            productMapper.updateByPrimaryKey(product);
        }
        // 删除购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.delete(shoppingCart);
    }

    @Override
    public List<List<OrderVO>> getOrder(Integer userId) {
        List<OrderVO> orderVOList = orderMapper.getOrderVOByUserId(userId);
        if (ArrayUtils.isEmpty(orderVOList.toArray())) {
            throw new MyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        // 将同一个订单放在一组
        Map<String, List<OrderVO>> collect = orderVOList.stream().collect(Collectors.groupingBy(Order::getOrderId));
        Collection<List<OrderVO>> values = collect.values();
        return new ArrayList<>(values);
    }
}
