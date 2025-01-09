package com.example.danjamserver.util.exception;

public class ChatRoomNotFoundException extends BaseException {
    public ChatRoomNotFoundException() {
        super(ResultCode.CAN_NOT_FOUND_CHATROOM);
    }
}
