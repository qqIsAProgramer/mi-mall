package com.qyl.mall.controller;

import com.qyl.mall.pojo.Product;
import com.qyl.mall.service.ProductService;
import com.qyl.mall.utils.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * 商品相关接口
 * @Author: qyl
 * @Date: 2020/12/6 12:54
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 通过分类获取商品
     * @param categoryId
     * @return
     */
    @GetMapping("/category")
    public ResponseEntity<List<Product>> getProductByCategoryId(Integer categoryId) {
        return productService.getProductByCategoryId(categoryId);
    }

    /**
     * 获取热门商品
     * @return
     */
    @GetMapping("/hot")
    public ResponseEntity<List<Product>> getHotProduct() {
        return productService.getHotProduct();
    }

    /**
     * 商品分页展示
     * @param currentPage 当前页数
     * @param pageSize 当前页展示的商品个数
     * @param categoryId
     * @return
     * @apiNote category为0时代表分页查询所有商品
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getProductByPage(Integer currentPage, Integer pageSize, Integer categoryId) {
        return productService.getProductByPage(currentPage, pageSize, categoryId);
    }
}
