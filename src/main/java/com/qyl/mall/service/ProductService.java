package com.qyl.mall.service;

import com.github.pagehelper.PageInfo;
import com.qyl.mall.pojo.Product;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 12:23
 */
public interface ProductService {

    /**
     * 通过分类查询商品
     * @param categoryId
     * @return
     */
    List<Product> getProductByCategoryId(Integer categoryId);

    /**
     * 获取热门商品
     * @return
     */
    List<Product> getHotProduct();

    /**
     * 通过商品ID获取商品
     * @param productId
     * @return
     */
    Product getProductById(Integer productId);

    /**
     * 商品分页展示
     * @param currentPage
     * @param pageSize
     * @param categoryId
     * @return
     */
    PageInfo<Product> getProductByPage(Integer currentPage, Integer pageSize, Integer categoryId);
}
