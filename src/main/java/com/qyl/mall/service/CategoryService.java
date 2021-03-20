package com.qyl.mall.service;

import com.qyl.mall.pojo.Category;
import com.qyl.mall.utils.ResponseEntity;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 11:24
 */
public interface CategoryService {

    /**
     * 获取所有分类
     * @return
     */
    ResponseEntity<List<Category>> getAll();
}
