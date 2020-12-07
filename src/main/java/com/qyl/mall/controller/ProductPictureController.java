package com.qyl.mall.controller;

import com.qyl.mall.Enums.ResponseEnum;
import com.qyl.mall.pojo.ProductPicture;
import com.qyl.mall.service.ProductPictureService;
import com.qyl.mall.utils.ResultMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: qyl
 * @Date: 2020/12/6 18:40
 */
@RestController
@RequestMapping("/picture")
public class ProductPictureController {

    @Resource
    private ProductPictureService productPictureService;

    @GetMapping("/{productId}")
    public ResultMessage productPicture(@PathVariable Integer productId) {
        List<ProductPicture> list = productPictureService.getProductPictureByProductId(productId);
        return ResultMessage.success(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), list);
    }
}
