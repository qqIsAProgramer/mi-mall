package com.qyl.mall.service.impl;

import com.qyl.mall.enums.ResponseEnum;
import com.qyl.mall.mapper.OrderMapper;
import com.qyl.mall.mapper.ProductMapper;
import com.qyl.mall.mapper.SeckillProductMapper;
import com.qyl.mall.mapper.ShoppingCartMapper;
import com.qyl.mall.pojo.Order;
import com.qyl.mall.pojo.Product;
import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.ShoppingCart;
import com.qyl.mall.service.OrderService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.utils.component.IdWorker;
import com.qyl.mall.utils.component.RedisKey;
import com.qyl.mall.utils.component.RedisUtil;
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

    @Resource
    private SeckillProductMapper seckillProductMapper;

    @Override
    @Transactional
    public ResponseEntity<Void> addOrder(List<CartVO> cartVOList, Integer userId) {
        // 添加订单
        String orderId = idWorker.nextId() + "";  // 订单ID
        long time = new Date().getTime();  // 订单生成时间
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

        return ResponseEntity.ok();
    }

    @Override
    public ResponseEntity<List<List<OrderVO>>> getOrder(Integer userId) {
        List<OrderVO> orderVOList = orderMapper.getOrderVOByUserId(userId);
        if (ArrayUtils.isEmpty(orderVOList.toArray())) {
            return ResponseEntity.error(ResponseEnum.ORDER_NOT_FOUND.getCode(), ResponseEnum.ORDER_NOT_FOUND.getMsg());
        }
        // 将同一个订单放在一组
        Collection<List<OrderVO>> values = orderVOList.stream().collect(Collectors.groupingBy(Order::getOrderId)).values();
        return ResponseEntity.ok(new ArrayList<>(values));
    }

    @Override
    @Transactional
    public void addSeckillOrder(Integer seckillId, Integer userId) {
        // 订单ID
        String orderId = idWorker.nextId() + "";
        // 商品ID
        SeckillProduct seckillProduct = new SeckillProduct();
        seckillProduct.setSeckillId(seckillId);
        SeckillProduct one = seckillProductMapper.selectOne(seckillProduct);
        Integer productId = one.getProductId();
        // 秒杀价格
        Double seckillPrice = one.getSeckillPrice();

        // 订单封装
        Order order = new Order();
        order.setOrderId(orderId);
        order.setProductId(productId);
        order.setProductPrice(seckillPrice);
        order.setProductNum(1);
        order.setUserId(userId);
        order.setOrderTime(new Date().getTime());

        orderMapper.insert(order);
        // 减库存
        seckillProductMapper.decrStock(seckillId);

        // 订单创建成功，将用户写入 redis，防止多次抢购
        RedisUtil.leftPush(RedisKey.SECKILL_PRODUCT_USER_LIST, userId);
    }
}
