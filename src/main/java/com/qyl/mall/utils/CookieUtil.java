package com.qyl.mall.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Author: qyl
 * @Date: 2020/12/5 15:23
 */
public class CookieUtil {

    /**
     * 设置cookie的值，并使其在指定时间内生效
     * @param cookieMaxAge cookie生效的最大秒数
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge) {
        if (cookieValue == null) {
            cookieValue = "";
        } else {
            cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);
        }

        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (cookieMaxAge != null && cookieMaxAge > 0) {
            cookie.setMaxAge(cookieMaxAge);
        }
        if (request != null) {
            cookie.setDomain(getDomainName(request));
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 得到cookie的域名
     */
    private static final String getDomainName(HttpServletRequest request) {
        String domainName;

        String serverName = request.getRequestURL().toString();
        if (serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName.indexOf(":") > 0) {
            String[] ary = domainName.split(":");
            domainName = ary[0];
        }
        return domainName;
    }

    /**
     * 删除Cookie
     */
    public static void delCookie(HttpServletRequest request, String cookieName) {
        // 获得cookie数组
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                // 删除前必须new一个value为空的
                Cookie c = new Cookie(cookieName, null);
                c.setPath("/");  // 路径要相同
                c.setMaxAge(0);  // 生命周期设为0
                break;
            }
        }
    }
}
