package com.example.danjamserver.util.exception;

public class FcmTopicNotFoundException extends BaseException{
    public FcmTopicNotFoundException() {
        super(ResultCode.FCM_NO_MATCHING_TOPIC);
    }
}
