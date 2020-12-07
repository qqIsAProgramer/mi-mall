package com.qyl.mall.exception;

import com.qyl.mall.Enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: qyl
 * @Date: 2020/12/5 9:43
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyException extends RuntimeException {

    private ExceptionEnum exceptionEnum;
}
