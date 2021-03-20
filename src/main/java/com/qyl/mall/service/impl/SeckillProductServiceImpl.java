package com.qyl.mall.service.impl;

import com.qyl.mall.enums.ResponseEnum;
import com.qyl.mall.mapper.SeckillProductMapper;
import com.qyl.mall.mapper.SeckillTimeMapper;
import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.SeckillTime;
import com.qyl.mall.service.SeckillProductService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.utils.component.BeanUtil;
import com.qyl.mall.utils.component.RedisKey;
import com.qyl.mall.utils.component.RedisUtil;
import com.qyl.mall.utils.component.TimeUtil;
import com.qyl.mall.vo.SeckillProductVO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: qyl
 * @Date: 2020/12/8 19:16
 */
@Service
public class SeckillProductServiceImpl implements SeckillProductService {

    @Resource
    private SeckillTimeMapper seckillTimeMapper;

    @Resource
    private SeckillProductMapper seckillProductMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 记录秒杀是否结束
     */
    private final Map<Integer, Boolean> localOverMap = new HashMap<>();

    @Override
    @Transactional
    public ResponseEntity<List<SeckillProductVO>> getSeckillProductByTime(Integer timeId) {
        // 先查看缓存，是否有列表
        List<SeckillProductVO> seckillProductVOList = RedisUtil.listRange(RedisKey.SECKILL_PRODUCT_LIST + timeId, 0, -1);
        if (ArrayUtils.isNotEmpty(seckillProductVOList.toArray())) {
            return ResponseEntity.ok(seckillProductVOList);
        }
        // 缓存中没有，再从数据库中获取，添加到缓存
        seckillProductVOList = seckillProductMapper.getSeckillProductVOList(timeId, new Date().getTime());
        if (ArrayUtils.isNotEmpty(seckillProductVOList.toArray())) {
            RedisUtil.leftPushAll(RedisKey.SECKILL_PRODUCT_LIST + timeId, seckillProductVOList);
            // 设置过期时间
            long timeout = seckillProductVOList.get(0).getEndTime() - new Date().getTime();
            RedisUtil.expire(RedisKey.SECKILL_PRODUCT_LIST + timeId, timeout, TimeUnit.MILLISECONDS);
            return ResponseEntity.ok(seckillProductVOList);
        }
        return ResponseEntity.error(ResponseEnum.SECKILL_NOT_FOUND.getCode(), ResponseEnum.SECKILL_NOT_FOUND.getMsg());
    }

    @Override
    public ResponseEntity<Void> addSeckillProduct(SeckillProduct seckillProduct) {
        // 添加秒杀商品
        Date time = TimeUtil.getTime();
        // 开始时间为当前时间的整点后一小时
        long startTime = time.getTime() + 1000 * 60 * 60;
        // 持续时间为一小时
        long endTime = startTime + 1000 * 60 * 60;
        SeckillTime seckillTime = new SeckillTime();
        seckillTime.setStartTime(startTime);
        seckillTime.setEndTime(endTime);
        // 查看是否有该时段
        SeckillTime one = seckillTimeMapper.selectOne(seckillTime);
        if (one == null) {
            seckillTimeMapper.insert(seckillTime);
            seckillProduct.setTimeId(seckillTime.getTimeId());
        } else {
            seckillProduct.setTimeId(one.getTimeId());
        }
        seckillProductMapper.insert(seckillProduct);
        return ResponseEntity.ok();
    }

    @Override
    public ResponseEntity<List<SeckillTime>> getSeckillTime() {
        // 获取当前时间及往后 8 个时间段
        Date time = TimeUtil.getTime();
        List<SeckillTime> seckillTimeList = seckillTimeMapper.getTime(time.getTime());
        return ResponseEntity.ok(seckillTimeList);
    }

    @Override
    public ResponseEntity<SeckillProductVO> getSeckillProductById(Integer seckillId) {
        // 从缓存中查询
        Map<String, Object> map = RedisUtil.getEntries(RedisKey.SECKILL_PRODUCT_ID + seckillId);
        if (!map.isEmpty()) {
            try {
                SeckillProductVO seckillProductVO = BeanUtil.map2bean(map, SeckillProductVO.class);
                return ResponseEntity.ok(seckillProductVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 从数据库中查询
        SeckillProductVO seckillProductVO = seckillProductMapper.getSeckillProductById(seckillId);
        if (seckillProductVO != null) {
            try {
                RedisUtil.putAll(RedisKey.SECKILL_PRODUCT_ID + seckillId, BeanUtil.bean2map(seckillProductVO));
                RedisUtil.expire(RedisKey.SECKILL_PRODUCT_ID + seckillId, seckillProductVO.getEndTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
                // 将库存单独存一个key
                if (RedisUtil.getValue(RedisKey.SECKILL_PRODUCT_STOCK_ID + seckillId) == null) {
                    RedisUtil.setValue(RedisKey.SECKILL_PRODUCT_STOCK_ID + seckillId, seckillProductVO.getSeckillStock(), seckillProductVO.getEndTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(seckillProductVO);
        }
        return ResponseEntity.fail();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> doSeckillProduct(Integer seckillId, Integer userId) {
        if (localOverMap.get(seckillId) != null && localOverMap.get(seckillId)) {
            return ResponseEntity.error(ResponseEnum.SECKILL_IS_OVER.getCode(), ResponseEnum.SECKILL_IS_OVER.getMsg());
        }
        // 判断秒杀是否开始，防止路径暴露被刷
        Map<String, Object> map = RedisUtil.getEntries(RedisKey.SECKILL_PRODUCT_ID + seckillId);
        if (!map.isEmpty()) {
            SeckillProductVO seckillProductVO = null;
            try {
                seckillProductVO = BeanUtil.map2bean(map, SeckillProductVO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 秒杀时间开始
            Long startTime = seckillProductVO.getStartTime();
            if (startTime > new Date().getTime()) {
                return ResponseEntity.error(ResponseEnum.SECKILL_NOT_START.getCode(), ResponseEnum.SECKILL_NOT_START.getMsg());
            }
        }

        // 判断是否已经秒杀过了，避免一个账户秒杀多个商品
        List<String> list = RedisUtil.listRange(RedisKey.SECKILL_PRODUCT_LIST + seckillId, 0, -1);
        if (list.contains(String.valueOf(userId))) {
            return ResponseEntity.error(ResponseEnum.SECKILL_IS_REUSE.getCode(), ResponseEnum.SECKILL_IS_REUSE.getMsg());
        }

        // 预减库存：从缓存中减去库存
        // 利用redis中的方法，减去库存，返回值为减去1的值
        if (RedisUtil.decrement(RedisKey.SECKILL_PRODUCT_STOCK_ID + seckillId) < 0) {
            // 设置内存标记
            localOverMap.put(seckillId, true);
            // 秒杀完成，库存为空
            return ResponseEntity.error(ResponseEnum.SECKILL_IS_OVER.getCode(), ResponseEnum.SECKILL_IS_OVER.getMsg());
        }

        // 使用RabbitMQ异步传输
        MQSend(seckillId, userId);

        return ResponseEntity.ok();
    }


    @Override
    public Long getEndTime(Integer seckillId) {
        SeckillProductVO seckillProductVO = seckillProductMapper.getSeckillProductById(seckillId);
        return seckillTimeMapper.getEndTime(seckillProductVO.getTimeId());
    }

    /**
     * 通过 MQ 发送消息
     * @param seckillId
     * @param userId
     */
    private void MQSend(Integer seckillId, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("userId", userId);
        // 设置ID，保证消息队列等幂性
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(seckillId + ":" + userId);
        try {
            rabbitTemplate.convertAndSend("seckill_order", map, correlationData);
        } catch (Exception e) {
            // 发送消息失败
            e.printStackTrace();
            RedisUtil.increment(RedisKey.SECKILL_PRODUCT_STOCK_ID + seckillId);
        }
    }

}
