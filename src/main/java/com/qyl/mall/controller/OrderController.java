package com.qyl.mall.controller;

import com.qyl.mall.service.OrderService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.utils.component.RedisUtil;
import com.qyl.mall.vo.CartVO;
import com.qyl.mall.vo.OrderVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单相关接口
 * @Author: qyl
 * @Date: 2020/12/11 9:25
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 添加订单
     * @param cartVOList
     * @param token
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addOrder(@RequestBody List<CartVO> cartVOList, @CookieValue("USER_TOKEN") String token) {
        // 判断 cookie 是否存在
        Integer userId = (Integer) RedisUtil.getValue(token, "userId");
        return orderService.addOrder(cartVOList, userId);
    }

    /**
     * 获取用户订单列表
     * @param token
     * @return
     */
    @GetMapping("/get")
    public ResponseEntity<List<List<OrderVO>>> getOrder(@CookieValue("USER_TOKEN") String token) {
        // 判断 cookie 是否存在
        Integer userId = (Integer) RedisUtil.getValue(token, "userId");
        return orderService.getOrder(userId);
    }
}
