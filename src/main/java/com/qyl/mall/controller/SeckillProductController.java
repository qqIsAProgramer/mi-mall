package com.qyl.mall.controller;

import com.qyl.mall.pojo.SeckillProduct;
import com.qyl.mall.pojo.SeckillTime;
import com.qyl.mall.service.SeckillProductService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.utils.component.RedisUtil;
import com.qyl.mall.vo.SeckillProductVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品秒杀相关接口
 * @Author: qyl
 * @Date: 2020/12/10 10:52
 */
@RestController
@RequestMapping("/seckill")
public class SeckillProductController {

    @Resource
    private SeckillProductService seckillProductService;

    /**
     * 通过时间 ID 来获取对应秒杀商品列表
     * @param timeId
     * @return
     */
    @GetMapping("/timeId")
    public ResponseEntity<List<SeckillProductVO>> getSeckillProductByTime(Integer timeId) {
        return seckillProductService.getSeckillProductByTime(timeId);
    }

    /**
     * 获取秒杀商品
     * @param seckillId
     * @return
     */
    @GetMapping("/seckillId")
    public ResponseEntity<SeckillProductVO> getSeckillProductById(Integer seckillId) {
        return seckillProductService.getSeckillProductById(seckillId);
    }

    /**
     * 获取秒杀时间
     * @return
     */
    @GetMapping("/time")
    public ResponseEntity<List<SeckillTime>> getSeckillTime() {
        return seckillProductService.getSeckillTime();
    }

    /**
     * 添加秒杀商品
     * @param seckillProduct
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addSeckillProduct(SeckillProduct seckillProduct) {
       return seckillProductService.addSeckillProduct(seckillProduct);
    }

    /**
     * 秒杀商品
     * @param seckillId
     * @param token
     * @return
     */
    @PostMapping("/do")
    public ResponseEntity<Void> doSeckillProduct(Integer seckillId, @CookieValue("USER_TOKEN") String token) {
        // 先判断 cookie 是否还存在
        Integer userId = (Integer) RedisUtil.getValue(token, "userId");
        return seckillProductService.doSeckillProduct(seckillId, userId);
    }
}
