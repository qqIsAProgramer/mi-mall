package com.qyl.mall.service.impl;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.UserMapper;
import com.qyl.mall.pojo.User;
import com.qyl.mall.service.UserService;
import com.qyl.mall.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: qyl
 * @Date: 2020/12/4 20:35
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void register(User user) {
        User u = new User();
        u.setUserPhoneNumber(user.getUserPhoneNumber());
        // 查看手机号是否被注册
        if (userMapper.selectCount(u) > 0) {
            // 手机号已存在
            throw new MyException(ExceptionEnum.SAVE_USER_REUSE);
        }
        // 密码加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword()));

        // 存入数据库
        userMapper.insert(user);
    }

    @Override
    public User login(User user) {
        // 将用户密码加密与数据库中的进行对比
        user.setPassword(MD5Util.MD5Encode(user.getPassword()));
        if (userMapper.selectOne(user) == null) {
            throw new MyException(ExceptionEnum.USER_NOT_FOUND);
        }
        return user;
    }
}
