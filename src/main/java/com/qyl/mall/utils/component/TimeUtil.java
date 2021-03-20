package com.qyl.mall.utils.component;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: qyl
 * @Date: 2021/3/8 22:23
 * @Description:
 */
public class TimeUtil {

    /**
     * 获取当前时间的整点
     * @return
     */
    public static Date getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
