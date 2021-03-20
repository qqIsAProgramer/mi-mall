package com.qyl.mall.utils.component;

import org.apache.tomcat.util.security.MD5Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: qyl
 * @Date: 2021/3/4 22:55
 * @Description: 加密工具类
 */
public class EncryptUtil {

    /**
     * 数据加密
     * @param origin
     * @return
     */
    public static String encryptByMD5(String origin) {
        String encrypt = "";
        try {
            // 确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 通过MD5加密
            encrypt = MD5Encoder.encode(md5.digest(origin.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encrypt;
    }
}
