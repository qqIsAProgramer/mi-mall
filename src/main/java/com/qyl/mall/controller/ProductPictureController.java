package com.qyl.mall.controller;

import com.qyl.mall.pojo.ProductPicture;
import com.qyl.mall.service.ProductPictureService;
import com.qyl.mall.utils.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品图片相关接口
 * @Author: qyl
 * @Date: 2020/12/6 18:40
 */
@RestController
@RequestMapping("/productPicture")
public class ProductPictureController {

    @Resource
    private ProductPictureService productPictureService;

    /**
     * 通过商品ID获取商品图片
     * @param productId
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<List<ProductPicture>> getProductPictureById(Integer productId) {
        return productPictureService.getProductPictureById(productId);
    }
}
