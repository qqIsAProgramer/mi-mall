package com.qyl.mall.controller;

import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.SeckillTime;
import com.qyl.mall.service.SeckillProductService;
import com.qyl.mall.utils.ResultMessage;
import com.qyl.mall.vo.SeckillProductVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/10 10:52
 */
@RestController
@RequestMapping("/seckill")
public class SeckillProductController {

    @Resource
    private SeckillProductService seckillProductService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 通过时间ID来获取对应秒杀商品列表
     * @param timeId
     * @return
     */
    @GetMapping("/time/{timeId}")
    public ResultMessage getProduct(@PathVariable Integer timeId) {
        List<SeckillProductVO> seckillProductVOList = seckillProductService.getProduct(timeId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), seckillProductVOList);
    }

    /**
     * 获取秒杀商品
     * @param seckillId
     * @return
     */
    @GetMapping("/{seckillId}")
    public ResultMessage getSeckill(@PathVariable Integer seckillId) {
        SeckillProductVO seckillProductVO = seckillProductService.getSeckill(seckillId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), seckillProductVO);
    }

    /**
     * 获取秒杀时间
     * @return
     */
    @GetMapping("/time")
    public ResultMessage getTime() {
        List<SeckillTime> seckillTimeList = seckillProductService.getTime();
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), seckillTimeList);
    }

    /**
     * 添加秒杀商品
     * @param seckillProduct
     * @return
     */
    @PostMapping("/add")
    public ResultMessage addSeckillProduct(SeckillProduct seckillProduct) {
        seckillProductService.addSeckillProduct(seckillProduct);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    @PostMapping("/product")
    public ResultMessage seckillProduct(Integer seckillId, @CookieValue("USER_TOKEN") String cookie) {
        // 先判断cookie是否还存在
        Integer userId = (Integer) redisTemplate.opsForHash().get(cookie, "userId");
        seckillProductService.seckillProduct(seckillId, userId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }
}
