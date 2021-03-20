package com.qyl.mall.service;

import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.vo.CartVO;
import com.qyl.mall.vo.OrderVO;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/11 9:05
 */
public interface OrderService {

    /**
     * 添加订单
     * @param cartVOList
     * @param userId
     */
    ResponseEntity<Void> addOrder(List<CartVO> cartVOList, Integer userId);

    /**
     * 查看订单列表
     * @param userId
     * @return
     */
    ResponseEntity<List<List<OrderVO>>> getOrder(Integer userId);

    /**
     * 添加秒杀订单
     * @param seckillId
     * @param userId
     */
    void addSeckillOrder(Integer seckillId, Integer userId);
}
