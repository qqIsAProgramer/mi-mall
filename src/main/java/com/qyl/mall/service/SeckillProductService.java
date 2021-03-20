package com.qyl.mall.service;

import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.SeckillTime;
import com.qyl.mall.utils.ResponseEntity;
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
    ResponseEntity<List<SeckillProductVO>> getSeckillProductByTime(Integer timeId);

    /**
     * 添加秒杀商品
     * @param seckillProduct
     */
    ResponseEntity<Void> addSeckillProduct(SeckillProduct seckillProduct);

    /**
     * 获取秒杀时间列表
     * @return
     */
    ResponseEntity<List<SeckillTime>> getSeckillTime();

    /**
     * 获取单个秒杀商品
     * @param seckillId
     * @return
     */
    ResponseEntity<SeckillProductVO> getSeckillProductById(Integer seckillId);

    /**
     * 秒杀商品
     * @param seckillId
     * @param userId
     */
    ResponseEntity<Void> doSeckillProduct(Integer seckillId, Integer userId);

    /**
     * 获取秒杀结束时间
     * @param seckillId
     * @return
     */
    Long getEndTime(Integer seckillId);
}
