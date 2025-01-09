package com.example.danjamserver.chatroom.domain;

import com.example.danjamserver.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter @Setter
@IdClass(ChatRoomUserPK.class)
@Table(name = "chatRoomUser")
@NoArgsConstructor
public class ChatRoomUser {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;

    @Builder
    public ChatRoomUser(User user, ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
    }

    public static ChatRoomUser create(User user, ChatRoom chatRoom) {
        return ChatRoomUser.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }
}
