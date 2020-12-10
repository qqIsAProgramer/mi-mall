package com.qyl.mall.service.impl;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.SeckillProductMapper;
import com.qyl.mall.mapper.SeckillTimeMapper;
import com.qyl.mall.mq.Listener;
import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.SeckillTime;
import com.qyl.mall.service.SeckillProductService;
import com.qyl.mall.utils.BeanUtil;
import com.qyl.mall.utils.RedisKey;
import com.qyl.mall.vo.SeckillProductVO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private Listener listener;

    private final Map<Integer, Boolean> localOverMap = new HashMap<>();

    @Override
    @Transactional
    public List<SeckillProductVO> getProduct(Integer timeId) {
        // 先查看缓存，是否有列表
        List<SeckillProductVO> seckillProductVOList = redisTemplate.opsForList().range(RedisKey.SECKILL_PRODUCT_LIST + timeId, 0, -1);
        if (seckillProductVOList != null && ArrayUtils.isNotEmpty(seckillProductVOList.toArray())) {
            return seckillProductVOList;
        }
        // 缓存中没有，再从数据库中获取，添加到缓存
        seckillProductVOList = seckillProductMapper.getSeckillProductVOList(timeId, new Date().getTime());
        if (ArrayUtils.isNotEmpty(seckillProductVOList.toArray())) {
            redisTemplate.opsForList().leftPushAll(RedisKey.SECKILL_PRODUCT_LIST + timeId, seckillProductVOList);
            // 设置过期时间
            long expireTime = seckillProductVOList.get(0).getEndTime() - new Date().getTime();
            redisTemplate.expire(RedisKey.SECKILL_PRODUCT_LIST + timeId, expireTime, TimeUnit.MILLISECONDS);
        } else {
            // 秒杀商品不存在
            throw new MyException(ExceptionEnum.SECKILL_NOT_FOUND);
        }
        return seckillProductVOList;
    }

    @Override
    public void addSeckillProduct(SeckillProduct seckillProduct) {
        // 添加秒杀商品
        Date time = getDate();
        long startTime = time.getTime() + 1000 * 60 * 60;
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
    }

    /**
     * 获取当前时间的整点
     * @return
     */
    private Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public List<SeckillTime> getTime() {
        // 获取当前时间及往后8个时间段
        Date time = getDate();
        return seckillTimeMapper.getTime(time.getTime());
    }

    @Override
    public SeckillProductVO getSeckill(Integer seckillId) {
        // 从缓存中查询
        Map map = redisTemplate.opsForHash().entries(RedisKey.SECKILL_PRODUCT + seckillId);
        if (!map.isEmpty()) {
            SeckillProductVO seckillProductVO = null;
            try {
                seckillProductVO = BeanUtil.map2bean(map, SeckillProductVO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return seckillProductVO;
        }
        // 从数据库中查询
        SeckillProductVO seckillProductVO = seckillProductMapper.getSeckill(seckillId);
        if (seckillProductVO != null) {
            try {
                redisTemplate.opsForHash().putAll(RedisKey.SECKILL_PRODUCT + seckillId, BeanUtil.bean2map(seckillProductVO));
                redisTemplate.expire(RedisKey.SECKILL_PRODUCT + seckillId, seckillProductVO.getEndTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
                // 将库存单独存一个key
                if (redisTemplate.opsForValue().get(RedisKey.SECKILL_PRODUCT_STOCK + seckillId) == null) {
                    redisTemplate.opsForValue().set(RedisKey.SECKILL_PRODUCT_STOCK + seckillId, seckillProductVO.getSeckillStock(), seckillProductVO.getEndTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return seckillProductVO;
        }
        return null;
    }

    @Override
    public void seckillProduct(Integer seckillId, Integer userId) {
        if (localOverMap.get(seckillId) != null && !localOverMap.get(seckillId)) {
            throw new MyException(ExceptionEnum.SECKILL_IS_OVER);
        }
        // 判断秒杀是否开始，防止路径暴露被刷
        Map map = redisTemplate.opsForHash().entries(RedisKey.SECKILL_PRODUCT + seckillId);
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
                throw new MyException(ExceptionEnum.SECKILL_NOT_START);
            }
        }

        // 判断是否已经秒杀过了，避免一个账户秒杀多个商品
        List<String> list = redisTemplate.opsForList().range(RedisKey.SECKILL_PRODUCT_LIST + seckillId, 0, -1);
        if (list.contains(String.valueOf(userId))) {
            throw new MyException(ExceptionEnum.SECKILL_IS_REUSE);
        }

        // 预减库存：从缓存中减去库存
        // 利用redis中的方法，减去库存，返回值为减去1的值
        if (redisTemplate.opsForValue().decrement(RedisKey.SECKILL_PRODUCT_STOCK + seckillId) < 0) {
            // 设置内存标记
            localOverMap.put(seckillId, true);
            // 秒杀完成，库存为空
            throw new MyException(ExceptionEnum.SECKILL_IS_OVER);
        }

        // 使用RabbitMQ异步传输
        MQSend(seckillId, userId);
    }

    /**
     * 通过MQ发送消息
     * @param seckillId
     * @param userId
     */
    private void MQSend(Integer seckillId, Integer userId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("userId", userId);
        // 设置ID，保证消息队列等幂性
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(seckillId + ":" + userId);
        try {
            listener.sendMsg("seckill_order", map, correlationData);
            listener.receiveMsg(map);
        } catch (Exception e) {
            // 发送消息失败
            e.printStackTrace();
            stringRedisTemplate.opsForValue().increment(RedisKey.SECKILL_PRODUCT_STOCK + seckillId);
        }
    }
}
