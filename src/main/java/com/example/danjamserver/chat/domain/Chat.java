package com.example.danjamserver.chat.domain;

import com.example.danjamserver.util.entity.MongoBaseEntity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "chat")
@Getter
@NoArgsConstructor
public class Chat extends MongoBaseEntity {

    @Id
    private String id;
    private String message;
    private String sender;
    @Indexed
    private Long chatRoomId;
    private ChatType chatType;
    // 읽지 않은 멤버 목록을 업데이트하는 메서드
    @Setter
    private List<String> unreadMembers; // 읽지 않은 사용자 목록

    @Builder
    private Chat(String message, String sender, Long chatRoomId, ChatType chatType, List<String> unreadMembers) {
        this.message = message;
        this.sender = sender;
        this.chatRoomId = chatRoomId;
        this.chatType = chatType;
        this.unreadMembers = unreadMembers;
    }

}
