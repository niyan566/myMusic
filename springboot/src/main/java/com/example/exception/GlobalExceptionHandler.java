package com.example.exception;


import com.example.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        log.error(e.getMessage());
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e){
        return Result.error(e.getCode(),e.getMsg());
    }
}
