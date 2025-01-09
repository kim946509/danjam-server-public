package com.example.danjamserver.util.exception;

/**
 * 입력값이 잘못되었을 때 발생하는 예외
 */
public class InvalidInputException extends BaseException {

    // 특정한 메세지를 전달하지 않고, 기본적인 INVALID_INPUT 메세지를 전달
    public InvalidInputException() {
        super(ResultCode.INVALID_INPUT);
    }

    // 특정한 메세지를 전달하고 싶을 때 사용. ResultCode를 인자를 통해 전달.
    public InvalidInputException(ResultCode resultCode) {
        super(resultCode);
    }

    // 동적 메세지를 생성하고 싶을 때 사용.
    public InvalidInputException(String detailMessage) {
        super(ResultCode.INVALID_INPUT, detailMessage);
    }
}
