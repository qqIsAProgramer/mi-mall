package com.qyl.mall.service.impl;

import com.qyl.mall.enums.ResponseEnum;
import com.qyl.mall.mapper.CategoryMapper;
import com.qyl.mall.pojo.Category;
import com.qyl.mall.service.CategoryService;
import com.qyl.mall.utils.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 11:24
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categoryList = categoryMapper.selectAll();
        if (categoryList == null) {
            return ResponseEntity.error(ResponseEnum.CATEGORY_NOT_FOUND.getCode(), ResponseEnum.CATEGORY_NOT_FOUND.getMsg());
        }
        return ResponseEntity.ok(categoryList);
    }
}
