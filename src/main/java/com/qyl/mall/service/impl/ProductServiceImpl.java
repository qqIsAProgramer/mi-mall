package com.qyl.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.ProductMapper;
import com.qyl.mall.pojo.Product;
import com.qyl.mall.service.ProductService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 12:28
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Product> getProductByCategoryId(Integer categoryId) {
        // 设置模板
        Example example = new Example(Product.class);
        example.orderBy("productSales").desc();
        example.createCriteria().andEqualTo("categoryId", categoryId);
        PageHelper.startPage(0, 8);

        List<Product> productList = productMapper.selectByExample(example);
        if (ArrayUtils.isEmpty(productList.toArray())) {
            throw new MyException(ExceptionEnum.PRODUCT_NOT_FOUND);
        }
        return productList;
    }

    @Override
    public List<Product> getHotProduct() {
        Example example = new Example(Product.class);
        example.orderBy("productSales").desc();
        PageHelper.startPage(0, 8);

        List<Product> productList = productMapper.selectByExample(example);
        if (ArrayUtils.isEmpty(productList.toArray())) {
            throw new MyException(ExceptionEnum.PRODUCT_NOT_FOUND);
        }
        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new MyException(ExceptionEnum.PRODUCT_NOT_FOUND);
        }
        return product;
    }

    @Override
    public PageInfo<Product> getProductByPage(Integer currentPage, Integer pageSize, Integer categoryId) {
        List<Product> productList;
        PageHelper.startPage(currentPage - 1, pageSize, true);

        // 当category为0时代表分页查询所有商品
        if (categoryId == 0) {
            productList = productMapper.selectAll();
        } else {
            // 分类查询商品
            Product product = new Product();
            product.setCategoryId(categoryId);
            productList = productMapper.select(product);
        }
        return new PageInfo<>(productList);
    }
}
