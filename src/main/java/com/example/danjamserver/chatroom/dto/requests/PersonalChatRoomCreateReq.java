package com.example.danjamserver.chatroom.dto.requests;

import com.example.danjamserver.mate.domain.MateType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonalChatRoomCreateReq {

    private String friendUsername;
    private MateType chatRoomMateType;

    @Builder
    private PersonalChatRoomCreateReq(String friendUsername, MateType chatRoomMateType) {
        this.friendUsername = friendUsername;
        this.chatRoomMateType = chatRoomMateType;
    }
}
