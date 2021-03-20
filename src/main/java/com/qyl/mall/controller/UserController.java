package com.qyl.mall.controller;

import com.qyl.mall.enums.ResponseEnum;
import com.qyl.mall.pojo.User;
import com.qyl.mall.service.UserService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.utils.component.BeanUtil;
import com.qyl.mall.utils.component.CookieUtil;
import com.qyl.mall.utils.component.RedisUtil;
import com.qyl.mall.utils.component.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关接口
 * @Author: qyl
 * @Date: 2020/12/5 10:54
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(User user) {
        return userService.register(user);
    }

    /**
     * 用户登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        user = userService.login(user);
        if (user == null) {
            return ResponseEntity.error(ResponseEnum.USER_NOT_FOUND.getCode(), ResponseEnum.USER_NOT_FOUND.getMsg());
        }
        // 添加 cookie，设置唯一认证
        String token = TokenUtil.genToken(user.getPhone());
        CookieUtil.setCookie(request, response, "USER_TOKEN", token, 60 * 60);

        // 将 encode 放入 redis 中，用于认证
        RedisUtil.putAll(token, BeanUtil.bean2map(user));
        RedisUtil.expire(token, 60 * 60, TimeUnit.SECONDS);

        return ResponseEntity.ok(user);
    }

    /**
     * 根据 cookie 获取用户信息
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/token")
    public ResponseEntity<User> getUserByToken(@CookieValue("USER_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> userMap = RedisUtil.getEntries(token);
        /*
        map 可能为空，即 redis 中已过期，但 cookie 还存在
        这个时候就要删去 cookie，让用户重新登录
         */
        if (userMap.isEmpty()) {
            CookieUtil.delCookie(request, response, token);
            return ResponseEntity.error(ResponseEnum.TOKEN_EXPIRED.getCode(), ResponseEnum.TOKEN_EXPIRED.getMsg());
        }

        // 设置过期时间
        RedisUtil.expire(token, 60 * 60, TimeUnit.SECONDS);
        return ResponseEntity.ok(BeanUtil.map2bean(userMap, User.class));
    }
}
