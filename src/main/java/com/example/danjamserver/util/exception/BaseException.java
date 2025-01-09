package com.example.danjamserver.util.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{

    private final ResultCode resultCode;
    public BaseException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BaseException(ResultCode resultCode, String detailMessage) {
        super(detailMessage);
        this.resultCode = resultCode;
    }

}
