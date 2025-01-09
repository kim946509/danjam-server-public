package com.example.danjamserver.chat.repository;

import com.example.danjamserver.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByChatRoomId(Long chatRoomId);

    @Query(value = "{ 'chatRoomId' : ?0 }", sort = "{ 'createdDateTime' : -1 }")
    List<Chat> findLastMessageByChatRoomId(Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);

    List<Chat> findByChatRoomIdAndUnreadMembersContains(Long chatRoomId, String username);
}
