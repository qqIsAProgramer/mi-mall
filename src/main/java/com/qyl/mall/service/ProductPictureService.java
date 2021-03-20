package com.qyl.mall.service;

import com.qyl.mall.pojo.ProductPicture;
import com.qyl.mall.utils.ResponseEntity;

import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 18:27
 */
public interface ProductPictureService {

    /**
     * 通过商品ID获取商品图片
     * @param productId
     * @return
     */
    ResponseEntity<List<ProductPicture>> getProductPictureById(Integer productId);
}
