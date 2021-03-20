package com.qyl.mall.service;

import com.qyl.mall.pojo.User;
import com.qyl.mall.utils.ResponseEntity;

/**
 * @Author: qyl
 * @Date: 2020/12/4 20:35
 */
public interface UserService {

    /**
     * 用户注册
     * @param user
     */
    ResponseEntity<Void> register(User user);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User login(User user);
}
