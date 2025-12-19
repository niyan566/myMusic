package com.example.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private String code;
    private String msg;

    public CustomException(String code,String msg){
      this.code=code;
      this.msg=msg;
    }
}
