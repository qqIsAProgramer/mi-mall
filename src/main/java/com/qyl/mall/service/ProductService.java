package com.qyl.mall.service;

import com.qyl.mall.pojo.Product;
import com.qyl.mall.utils.ResponseEntity;

import java.util.List;
import java.util.Map;

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
    ResponseEntity<List<Product>> getProductByCategoryId(Integer categoryId);

    /**
     * 获取热门商品
     * @return
     */
    ResponseEntity<List<Product>> getHotProduct();

    /**
     * 商品分页展示
     * @param currentPage
     * @param pageSize
     * @param categoryId
     * @return
     */
    ResponseEntity<Map<String, Object>> getProductByPage(Integer currentPage, Integer pageSize, Integer categoryId);
}
