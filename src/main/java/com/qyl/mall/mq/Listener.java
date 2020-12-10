package com.qyl.mall.mq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: qyl
 * @Date: 2020/12/10 14:51
 */
@Component
@RabbitListener(queuesToDeclare = @Queue("seckill_order"))
public class Listener {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String routingKey, Object message, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(routingKey, message, correlationData);
    }

    @RabbitHandler
    public void receiveMsg(Object message) {
        System.out.println("message: " + message);
    }
}
