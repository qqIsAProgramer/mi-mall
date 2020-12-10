package com.qyl.mall.mapper;

import com.qyl.mall.pojo.Order;
import com.qyl.mall.vo.OrderVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/8 14:58
 */
@org.apache.ibatis.annotations.Mapper
public interface OrderMapper extends Mapper<Order> {

    @Select("select order.*, product.product_name, product.product_picture " +
            "from order, product " +
            "where order.product_id = product.product_id and order.user_id = #{userId}")
    List<OrderVO> getOrderVOByUserId(Integer userId);
}
