package com.qyl.mall.service.impl;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.exception.MyException;
import com.qyl.mall.mapper.ProductPictureMapper;
import com.qyl.mall.pojo.ProductPicture;
import com.qyl.mall.service.ProductPictureService;
import org.apache.commons.lang.ArrayUtils;
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
    public List<ProductPicture> getProductPictureByProductId(Integer productId) {
        ProductPicture productPicture = new ProductPicture();
        productPicture.setProductId(productId);
        List<ProductPicture> productPictureList = productPictureMapper.select(productPicture);
        if (ArrayUtils.isEmpty(productPictureList.toArray())) {
            throw new MyException(ExceptionEnum.PICTURE_NOT_FOUND);
        }
        return productPictureList;
    }
}
