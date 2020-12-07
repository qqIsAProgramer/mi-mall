package com.qyl.mall.service.impl;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.CategoryMapper;
import com.qyl.mall.pojo.Category;
import com.qyl.mall.service.CategoryService;
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
    public List<Category> getAll() {
        List<Category> categories = categoryMapper.selectAll();
        if (categories == null) {
            throw new MyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categories;
    }
}
