package com.example.danjamserver.chatroom.dto.requests;

import com.example.danjamserver.mate.domain.MateType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GroupChatRoomCreateReq {

    private List<String> friendUsernames;
    private MateType chatRoomMateType;

    @Builder
    private GroupChatRoomCreateReq(List<String> friendUsernames, MateType chatRoomMateType) {

        this.friendUsernames = friendUsernames;
        this.chatRoomMateType = chatRoomMateType;
    }
}
