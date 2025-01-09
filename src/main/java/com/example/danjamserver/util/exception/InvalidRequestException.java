package com.example.danjamserver.util.exception;

// 400 Bad Request. 잘못된 요청
public class InvalidRequestException extends BaseException{

    public InvalidRequestException(ResultCode resultCode) {
        super(resultCode);
    }
}
