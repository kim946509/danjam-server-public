package com.example.danjamserver.util.exception;

/**
 * 존재하지 않는 리소스에 접근했을 때 발생하는 예외
 */
public class InvalidResourceException extends BaseException {
    // 특정한 메세지를 전달하지 않고, 기본적인 CAN_NOT_FIND_RESOURCE 메세지를 전달
    public InvalidResourceException() {
        super(ResultCode.CAN_NOT_FIND_RESOURCE);
    }

    // 특정한 메세지를 전달하고 싶을 때 사용. ResultCode를 인자를 통해 전달.
    public InvalidResourceException(ResultCode resultCode) {
        super(resultCode);
    }
}
