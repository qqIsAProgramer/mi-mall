package com.qyl.mall.service.impl;

import com.qyl.mall.enums.ResponseEnum;
import com.qyl.mall.mapper.ProductPictureMapper;
import com.qyl.mall.pojo.ProductPicture;
import com.qyl.mall.service.ProductPictureService;
import com.qyl.mall.utils.ResponseEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 18:29
 */
@Service
public class ProductPictureServiceImpl implements ProductPictureService {

    @Resource
    private ProductPictureMapper productPictureMapper;

    @Override
    public ResponseEntity<List<ProductPicture>> getProductPictureById(Integer productId) {
        ProductPicture productPicture = new ProductPicture();
        productPicture.setProductId(productId);
        List<ProductPicture> productPictureList = productPictureMapper.select(productPicture);

        if (ArrayUtils.isEmpty(productPictureList.toArray())) {
            return ResponseEntity.error(ResponseEnum.PICTURE_NOT_FOUND.getCode(), ResponseEnum.PICTURE_NOT_FOUND.getMsg());
        }
        return ResponseEntity.ok(productPictureList);
    }
}
