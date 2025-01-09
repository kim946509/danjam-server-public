package com.example.danjamserver.util.exception;

// S3 파일을 가져오는 과정에서 오류가 발생했을 때 발생하는 예외
public class S3ServerException extends BaseException{

    public S3ServerException() {
        super(ResultCode.INTERNAL_SERVER_ERROR);
    }

    public S3ServerException(ResultCode resultCode) {
        super(resultCode);
    }
}
