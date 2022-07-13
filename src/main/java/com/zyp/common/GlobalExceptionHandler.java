package com.zyp.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
//拦截加了注解 RestController.class、 Controller 的类
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.warn("发生了一次sql异常："+ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            return R.error(split[2]+" 账号已存在!");
        }
        return R.error("发生了未知的sql错误!"+ex.getMessage());
    }

    /**
     * 自定义异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.warn("发生了一次业务异常："+ex.getMessage());

        return R.error(ex.getMessage());

    }
    /**
     * 自定义异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    public R<String> exceptionHandler(IOException ex){
        log.error("发生了一次IO异常："+ex.getMessage());
        return R.error("文件下载失败");
    }

    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception ex){
        log.error("Exception异常："+ex.getMessage());
        return R.error("Exception异常");
    }
}















