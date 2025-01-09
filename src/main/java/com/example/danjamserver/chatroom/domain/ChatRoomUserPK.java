package com.example.danjamserver.chatroom.domain;

import java.io.Serializable;
import java.util.Objects;

public class ChatRoomUserPK implements Serializable {

    private Long user;
    private Long chatRoom;

    public ChatRoomUserPK() {}

    public ChatRoomUserPK(Long user, Long chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomUserPK that = (ChatRoomUserPK) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(chatRoom, that.chatRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, chatRoom);
    }
}
