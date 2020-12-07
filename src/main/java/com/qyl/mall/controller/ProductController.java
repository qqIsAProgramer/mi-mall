package com.qyl.mall.controller;

import com.github.pagehelper.PageInfo;
import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.pojo.Product;
import com.qyl.mall.service.ProductService;
import com.qyl.mall.utils.ResultMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: qyl
 * @Date: 2020/12/6 12:54
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/category/{categoryId}")
    public ResultMessage getProductByCategoryId(@PathVariable Integer categoryId) {
        List<Product> productList = productService.getProductByCategoryId(categoryId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), productList);
    }

    @GetMapping("/hot")
    public ResultMessage getHotProduct() {
        List<Product> productList = productService.getHotProduct();
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), productList);
    }

    @GetMapping("/get/{productId}")
    public ResultMessage getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), product);
    }

    @GetMapping("/page")
    public Map<String, Object> getProductByPage(Integer currentPage, Integer pageSize, Integer categoryId) {
        PageInfo<Product> pageInfo = productService.getProductByPage(currentPage, pageSize, categoryId);
        Map<String, Object> map = new HashMap<>();
        map.put("code", ResponseEnum.SUCCESS.getCode());
        map.put("data", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        return map;
    }
}
