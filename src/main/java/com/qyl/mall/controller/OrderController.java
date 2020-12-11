package com.qyl.mall.controller;

import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.service.OrderService;
import com.qyl.mall.utils.ResultMessage;
import com.qyl.mall.vo.CartVO;
import com.qyl.mall.vo.OrderVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/11 9:25
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/add")
    public ResultMessage addOrder(@RequestBody List<CartVO> cartVOList, @CookieValue("USER_TOKEN") String cookie) {
        // 判断cookie是否存在
        Integer userId = (Integer) redisTemplate.opsForHash().get(cookie, "userId");
        orderService.addOrder(cartVOList, userId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    @GetMapping("/get")
    public ResultMessage getOrder(@CookieValue("USER_TOKEN") String cookie) {
        // 判断cookie是否存在
        Integer userId = (Integer) redisTemplate.opsForHash().get(cookie, "userId");
        List<List<OrderVO>> list = orderService.getOrder(userId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), list);
    }
}
