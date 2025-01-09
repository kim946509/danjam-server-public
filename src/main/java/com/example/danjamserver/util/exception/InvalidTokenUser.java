package com.example.danjamserver.util.exception;

import lombok.Getter;

@Getter
public class InvalidTokenUser extends BaseException {
    public InvalidTokenUser() {
        super(ResultCode.INVALID_TOKEN);
    }
}
