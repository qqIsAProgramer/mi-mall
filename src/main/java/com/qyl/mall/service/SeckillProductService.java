package com.qyl.mall.service;

import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.SeckillTime;
import com.qyl.mall.vo.SeckillProductVO;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/8 18:56
 */
public interface SeckillProductService {

    /**
     * 根据时间获取所有的秒杀商品
     * @param timeId
     * @return
     */
    List<SeckillProductVO> getProduct(Integer timeId);

    /**
     * 添加秒杀商品
     * @param seckillProduct
     */
    void addSeckillProduct(SeckillProduct seckillProduct);

    /**
     * 获取秒杀时间列表
     * @return
     */
    List<SeckillTime> getTime();

    /**
     * 获取单个秒杀商品
     * @param seckillId
     * @return
     */
    SeckillProductVO getSeckill(Integer seckillId);

    /**
     * 秒杀商品
     * @param seckillId
     * @param userId
     */
    void seckillProduct(Integer seckillId, Integer userId);
}
