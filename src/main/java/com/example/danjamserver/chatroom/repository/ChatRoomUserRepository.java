package com.example.danjamserver.chatroom.repository;

import com.example.danjamserver.chatroom.domain.ChatRoom;
import com.example.danjamserver.chatroom.domain.ChatRoomUser;
import com.example.danjamserver.chatroom.domain.ChatRoomUserPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, ChatRoomUserPK> {

    @Query("SELECT c FROM ChatRoom c "
            + "JOIN ChatRoomUser cu ON c.id = cu.chatRoom.id "
            + "GROUP BY c.id "
            + "HAVING COUNT(cu.chatRoom.id) = 2 AND "
            + "SUM(CASE WHEN cu.user.id IN (:userId, :friendId) THEN 1 ELSE 0 END) = 2")
    Optional<ChatRoom> findChatRoomByOnlyTwoUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);

    boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    @Query("SELECT cu.chatRoom FROM ChatRoomUser cu WHERE cu.user.username = :username")
    List<ChatRoom> findChatRoomsByUsername(@Param("username") String username);

    Optional<ChatRoomUser> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    long countByChatRoomId(Long chatRoomId);

    @Query("SELECT u.username FROM ChatRoomUser cu JOIN cu.user u WHERE cu.chatRoom.id = :chatRoomId")
    List<String> findAllUsersByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
