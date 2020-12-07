package com.qyl.mall.service;

import com.qyl.mall.pojo.ProductPicture;

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
    List<ProductPicture> getProductPictureByProductId(Integer productId);
}
