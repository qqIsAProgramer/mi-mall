package com.qyl.mall.mq;

import com.qyl.mall.service.OrderService;
import com.qyl.mall.service.SeckillProductService;
import com.qyl.mall.utils.component.RedisKey;
import com.qyl.mall.utils.component.RedisUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: qyl
 * @Date: 2021/3/6 21:40
 * @Description:
 */
@Component
public class SeckillOrderQueue {

    @Resource
    private OrderService orderService;

    @Resource
    private SeckillProductService seckillProductService;

    @RabbitListener(queues = "seckill_order")
    public void insertOrder(Map<String, Object> map, Channel channel, Message message) {
        // 查看id，保证等幂性
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (RedisUtil.hasKey(RedisKey.SECKILL_RABBITMQ_ID + correlationId)) {
            // redis 中存在，表明此条消息已消费，请勿重复消费
            return;
        }
        // 存入 redis，只需要判断是否存在，所以 value 为多少无所谓
        String key = RedisKey.SECKILL_RABBITMQ_ID + correlationId;
        RedisUtil.setValue(key, "*");

        Integer userId = (Integer) map.get("userId");
        Integer seckillId = (Integer) map.get("seckillId");
        Long endTime = seckillProductService.getEndTime(seckillId);
        // 设置过期时间
        RedisUtil.expire(key, endTime - new Date().getTime(), TimeUnit.MILLISECONDS);

        orderService.addSeckillOrder(seckillId, userId);
    }
}
