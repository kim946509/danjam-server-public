package com.example.danjamserver.util.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(ResultCode.NO_MATCHING_USER_FOUND);
    }

    public UserNotFoundException(String detailMessage) {
        super(ResultCode.NO_MATCHING_USER_FOUND, detailMessage);
    }
}
