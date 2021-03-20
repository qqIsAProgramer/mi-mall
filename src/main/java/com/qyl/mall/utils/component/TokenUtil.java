package com.qyl.mall.utils.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;

/**
 * @Author: qyl
 * @Date: 2021/2/9 11:38
 * @Description: token生成工具类
 */
public class TokenUtil {

    private static final String signature = "Q!w2XS%^63p*";

    private static final String PAYLOAD_NAME = "user_phone";

    /**
     * 通过用户手机号生成 token
     * @param phone
     * @return
     */
    public static String genToken(String phone) {
        // 生成token
        String token = JWT.create()
                .withClaim(PAYLOAD_NAME, phone)
                .sign(Algorithm.HMAC256(signature));

        return token;
    }

    /**
     * 验证 token
     * 若验证出错将会抛出异常
     * @param token
     * @return 用户信息 (userPhone)
     */
    public static String verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(signature))
                .build()
                .verify(token)
                .getClaim(PAYLOAD_NAME)
                .asString();
    }
}
