package com.qyl.mall.service;

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
    void addOrder(List<CartVO> cartVOList, Integer userId);

    /**
     * 查看订单列表
     * @param userId
     * @return
     */
    List<List<OrderVO>> getOrder(Integer userId);
}
