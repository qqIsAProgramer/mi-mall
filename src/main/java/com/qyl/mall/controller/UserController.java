package com.qyl.mall.controller;

import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.pojo.User;
import com.qyl.mall.service.UserService;
import com.qyl.mall.utils.BeanUtil;
import com.qyl.mall.utils.CookieUtil;
import com.qyl.mall.utils.MD5Util;
import com.qyl.mall.utils.ResultMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: qyl
 * @Date: 2020/12/5 10:54
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResultMessage register(User user) {
        userService.register(user);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    /**
     * 用户登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResultMessage login(User user, HttpServletRequest request, HttpServletResponse response) {
        user = userService.login(user);
        // 添加cookie，设置唯一认证
        String encode = MD5Util.MD5Encode(user.getUsername() + user.getPassword());
        // 加盐
        encode += "|" + user.getUserId() + "|" + user.getUsername() + "|";
        CookieUtil.setCookie(request, response, "USER_TOKEN", encode, 60 * 30);

        // 将encode放入redis中，用于认证
        try {
            redisTemplate.opsForHash().putAll(encode, BeanUtil.bean2map(user));
            redisTemplate.expire(encode, 60 * 30, TimeUnit.SECONDS);  // 设置过期时间
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将密码设置成null返回给前端
        user.setPassword(null);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), user);
    }

    /**
     * 根据token获取用户信息
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/token")
    public ResultMessage getUserByToken(@CookieValue("USER_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = redisTemplate.opsForHash().entries(token);
        /*
        可能map为空，即redis中时间已过期，但cookie还存在
        这个时候就要删去cookie，让用户重新登录
         */
        if (map.isEmpty()) {
            CookieUtil.delCookie(request, token);
            return ResultMessage.success(ResponseEnum.TOKEN_EXPIRED.getCode(), ResponseEnum.TOKEN_EXPIRED.getMsg());
        }

        // 设置过期时间
        redisTemplate.expire(token, 60 * 30, TimeUnit.SECONDS);
        User user = BeanUtil.map2bean(map, User.class);
        user.setPassword(null);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), user);
    }
}
