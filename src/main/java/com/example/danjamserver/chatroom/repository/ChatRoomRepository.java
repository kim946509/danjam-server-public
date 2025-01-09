package com.example.danjamserver.chatroom.repository;

import com.example.danjamserver.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c.title FROM ChatRoom AS c WHERE c.id = :chatRoomId")
    String findByTitle(@Param("chatRoomId") Long chatRoomId);
}
