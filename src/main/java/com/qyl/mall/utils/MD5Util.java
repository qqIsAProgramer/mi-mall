package com.qyl.mall.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author: qyl
 * @Date: 2020/12/5 10:21
 */
public class MD5Util {

    /**
     * MD5加密
     * @param origin
     * @return
     */
    public static String MD5Encode(String origin) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            result = byteArrayToHexString(md5.digest(origin.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(byteToHexString(b));
        }
        return sb.toString();
    }
}
