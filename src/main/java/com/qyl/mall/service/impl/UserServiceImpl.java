package com.qyl.mall.service.impl;

import com.qyl.mall.enums.ResponseEnum;
import com.qyl.mall.mapper.UserMapper;
import com.qyl.mall.pojo.User;
import com.qyl.mall.service.UserService;
import com.qyl.mall.utils.ResponseEntity;
import com.qyl.mall.utils.component.EncryptUtil;
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
    public ResponseEntity<Void> register(User user) {
        User u = new User();
        u.setPhone(user.getPhone());
        // 查看手机号是否被注册
        if (userMapper.selectCount(u) > 0) {
            // 手机号已存在
            return ResponseEntity.error(ResponseEnum.USER_EXIST.getCode(), ResponseEnum.USER_EXIST.getMsg());
        }
        // 密码加密
        user.setPassword(EncryptUtil.encryptByMD5(user.getPassword()));

        // 存入数据库
        userMapper.insertSelective(user);
        return ResponseEntity.ok();
    }

    @Override
    public User login(User user) {
        // 将用户密码加密与数据库中的进行对比
        user.setPassword(EncryptUtil.encryptByMD5(user.getPassword()));
        return userMapper.selectOne(user);
    }
}
