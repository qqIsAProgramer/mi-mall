package com.qyl.mall.exception;

import com.qyl.mall.Enums.ExceptionEnum;
import com.qyl.mall.utils.ResultMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: qyl
 * @Date: 2020/12/5 9:44
 */
@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {

    /**
     * 通用异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public ResultMessage handlerException(MyException e) {
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResultMessage.fail(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }
}
