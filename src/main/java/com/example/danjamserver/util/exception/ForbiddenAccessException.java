package com.example.danjamserver.util.exception;

import lombok.Getter;

// 403 Forbidden
@Getter
public class ForbiddenAccessException extends BaseException {
    /**
     * 해당 요청에 대한 권한이 없을 때 발생하는 예외
     */
    public ForbiddenAccessException() {
        super(ResultCode.CAN_NOT_ACCESS_RESOURCE);
    } // 해당 리소스에 대한 접근 권한이 없습니다.

    public ForbiddenAccessException(ResultCode resultCode) {
        super(resultCode);
    }
}
