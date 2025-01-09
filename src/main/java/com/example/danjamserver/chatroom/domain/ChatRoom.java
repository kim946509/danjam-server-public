package com.example.danjamserver.chatroom.domain;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.util.entity.BaseTimeEntity;
import com.example.danjamserver.chatroom.listener.ChatRoomListener;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(ChatRoomListener.class)
@Table(name = "chatRoom")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MateType chatRoomMateType;

    @Column(nullable = false)
    private String title;

    @Builder
    private ChatRoom(String title, MateType chatRoomMateType) {
        this.title = title;
        this.chatRoomMateType = chatRoomMateType;
    }
}
