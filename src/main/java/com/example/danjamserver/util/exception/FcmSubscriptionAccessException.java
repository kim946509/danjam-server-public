package com.example.danjamserver.util.exception;

public class FcmSubscriptionAccessException extends BaseException{
    public FcmSubscriptionAccessException() {
        super(ResultCode.FCM_SUBSCRIPTION_ERROR);
    }
}
