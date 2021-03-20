package com.qyl.mall.controller;

import com.qyl.mall.pojo.Category;
import com.qyl.mall.service.CategoryService;

import com.qyl.mall.utils.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类相关接口
 * @Author: qyl
 * @Date: 2020/12/6 11:22
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<Category>> category() {
        return categoryService.getAll();
    }
}
