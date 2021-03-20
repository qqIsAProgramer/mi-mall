package com.qyl.mall.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: qyl
 * @Date: 2020/12/7 14:46
 */
@Data
public class SeckillTime {

    /**
     * 秒杀时间ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeId;

    /**
     * 秒杀开始时间
     */
    private Long startTime;

    /**
     * 秒杀结束时间
     */
    private Long endTime;
}
