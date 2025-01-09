package com.example.danjamserver.chatroom.service;

import com.example.danjamserver.chatroom.domain.ChatRoom;
import com.example.danjamserver.chatroom.domain.ChatRoomUser;
import com.example.danjamserver.chatroom.dto.requests.GroupChatRoomCreateReq;
import com.example.danjamserver.chatroom.dto.requests.PersonalChatRoomCreateReq;
import com.example.danjamserver.chatroom.dto.responses.ChatRoomLeaveRes;
import com.example.danjamserver.chatroom.dto.responses.GroupChatRoomCreateRes;
import com.example.danjamserver.chatroom.dto.responses.PersonalChatRoomCreateRes;
import com.example.danjamserver.chatroom.repository.ChatRoomRepository;
import com.example.danjamserver.chatroom.repository.ChatRoomUserRepository;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;

    public PersonalChatRoomCreateRes createPersonalChatRoom(PersonalChatRoomCreateReq req, User user) {
        User friend = findByUsername(req.getFriendUsername());

        if (friend == null) {
            throw new UserNotFoundException();
        }

        if (MateType.ROOMMATE.equals(req.getChatRoomMateType()) && !Objects.equals(friend.getGender(), user.getGender())) {
            throw new InvalidRequestException(ResultCode.NOT_EQUAL_GENDER);
        }

        ChatRoom chatRoom = chatRoomUserRepository
                .findChatRoomByOnlyTwoUsers(user.getId(), friend.getId())
                .orElseGet(() -> {
                    String title = String.format("%s와 %s의 채팅방", user.getNickname(),
                            friend.getNickname());
                    return saveChatRoom(title, req.getChatRoomMateType(), Arrays.asList(user, friend));
                });
        return PersonalChatRoomCreateRes.create(chatRoom.getId(), chatRoom.getTitle(), chatRoom.getChatRoomMateType().toString());
    }

    private ChatRoom saveChatRoom(String title, MateType chatRoomMateType, List<User> participants) {
        ChatRoom chatRoom = ChatRoom.builder()
            .title(title)
            .chatRoomMateType(chatRoomMateType)
            .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        System.out.println("Saved ChatRoom: " + savedChatRoom );

        List<ChatRoomUser> chatRoomUsers = new ArrayList<>();
        for (User participant : participants) {
            chatRoomUsers.add(ChatRoomUser.create(participant, savedChatRoom));
        }
        chatRoomUserRepository.saveAll(chatRoomUsers);
        System.out.println("Saved ChatRoomUsers: " + chatRoomUsers);

        return chatRoom;
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public GroupChatRoomCreateRes createGroupChatRoom(GroupChatRoomCreateReq req, User user) {
        List<User> participants = new ArrayList<>(req.getFriendUsernames().stream()
                .map(this::findByUsername).toList());
        participants.add(user);

        // 만약 MateType이 ROOMMATE라면 성별이 모두 같은지 확인
        if (MateType.ROOMMATE.equals(req.getChatRoomMateType())) {
            boolean allSameGender = participants.stream()
                    .map(User::getGender)
                    .distinct()
                    .count() == 1;

            if (!allSameGender) {
                throw new InvalidRequestException(ResultCode.NOT_EQUAL_GENDER);
            }


        }

        // 각 참여자의 username을 쉼표로 구분하여 title 생성
        String title = participants.stream()
                .map(User::getUsername) // User 객체에서 username을 추출
                .collect(Collectors.joining(", ")); // 쉼표로 구분

        ChatRoom chatRoom = saveChatRoom(title, req.getChatRoomMateType(), participants);
        return GroupChatRoomCreateRes.create(chatRoom.getId(), chatRoom.getTitle(), req.getChatRoomMateType().toString());
    }

    public ChatRoomLeaveRes leaveChatRoom(Long chatRoomId, User user) {
        // 채팅방 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        // 사용자가 채팅방에 속해 있는지 확인
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, user.getId())
                .orElseThrow(ForbiddenAccessException::new);

        // 채팅방에서 사용자 제거
        chatRoomUserRepository.delete(chatRoomUser);

        // 채팅방에 남은 사용자가 없으면 채팅방 삭제
        if (chatRoomUserRepository.countByChatRoomId(chatRoomId) == 0) {
            chatRoomRepository.deleteById(chatRoomId);
        }

        // ChatRoomLeaveRes 반환
        return ChatRoomLeaveRes.create(chatRoom.getTitle(), chatRoom.getId(),
                chatRoom.getChatRoomMateType().toString());
    }
}
