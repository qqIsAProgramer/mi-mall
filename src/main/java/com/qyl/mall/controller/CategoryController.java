package com.qyl.mall.controller;

import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.pojo.Category;
import com.qyl.mall.service.CategoryService;
import com.qyl.mall.utils.ResultMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 11:22
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("")
    public ResultMessage category() {
        List<Category> categories = categoryService.getAll();
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), categories);
    }
}
