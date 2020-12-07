package com.qyl.mall.utils;

import java.awt.image.Kernel;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: qyl
 * @Date: 2020/12/5 15:44
 */
public class BeanUtil {

    /**
     * 将javaBean对象封装到Map集合中
     * @param bean
     * @return
     */
    public static Map<String, Object> bean2map(Object bean) throws Exception {
        Map<String, Object> map = new HashMap<>();

        // 获取对象字节码信息，不要Object属性
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        // 获取bean对象中的所有属性
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            String key = pd.getName();  // 获取属性名
            Object value = pd.getReadMethod().invoke(bean);  // 调用getter()方法获取内容
            map.put(key, value);
        }
        return map;
    }

    public static <T> T map2bean(Map<String, Object> map, Class<T> cls) throws Exception {
        // 采用反射动态创建对象
        T obj = cls.getDeclaredConstructor().newInstance();
        // 获取对象字节码信息，不要Object属性
        BeanInfo beanInfo = Introspector.getBeanInfo(cls, Object.class);
        // 获取bean对象中的所有属性
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            String key = pd.getName();  // 获取属性名
            Object value = map.get(key);  // 获取属性值
            pd.getWriteMethod().invoke(obj, value);  // 调用属性setter()方法，设置到JavaBean对象中
        }
        return obj;
    }
}
