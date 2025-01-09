package com.example.danjamserver.util.exception;

public class FcmTokenNotFoundException extends BaseException{
    public FcmTokenNotFoundException() {
        super(ResultCode.FCM_NO_MATCHING_TOKEN);
    }
}
