package com.example.danjamserver.util.exception;


public class CustomValidationException extends BaseException {

    public CustomValidationException(ResultCode resultCode, String detailMessage) {
        //메세지를 동적으로 생성
        super(resultCode, detailMessage);
    }

    public CustomValidationException() {
        super(ResultCode.VALIDATION_ERROR);
    }
}
