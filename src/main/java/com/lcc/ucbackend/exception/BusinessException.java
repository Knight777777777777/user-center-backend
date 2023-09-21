package com.lcc.ucbackend.exception;

import com.lcc.ucbackend.common.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String decription;

    public BusinessException(String message, int code, String decription) {
        super(message);
        this.code = code;
        this.decription = decription;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.decription = errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode,String decription) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.decription = decription;
    }

    public int getCode() {
        return code;
    }

    public String getDecription() {
        return decription;
    }
}
