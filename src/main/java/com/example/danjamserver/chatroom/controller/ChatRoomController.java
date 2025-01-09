package com.example.danjamserver.chatroom.controller;

import com.example.danjamserver.chatroom.dto.requests.GroupChatRoomCreateReq;
import com.example.danjamserver.chatroom.dto.requests.PersonalChatRoomCreateReq;
import com.example.danjamserver.chatroom.dto.requests.UserSearchReq;
import com.example.danjamserver.chatroom.dto.responses.*;
import com.example.danjamserver.chatroom.service.ChatRoomCommandService;
import com.example.danjamserver.chatroom.service.ChatRoomQueryService;
import com.example.danjamserver.chatroom.service.UserSearchService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomCommandService chatRoomCommandService;
    private final ChatRoomQueryService chatRoomQueryService;
    private final UserSearchService userSearchService;

    @PostMapping("/personal")
    public ResponseEntity<ApiResponseData<PersonalChatRoomCreateRes>> createPersonalChatRoom(
            @Valid @RequestBody PersonalChatRoomCreateReq req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PersonalChatRoomCreateRes chatRoomRes = chatRoomCommandService.createPersonalChatRoom(req, userDetails.getUser());
        ApiResponseData<PersonalChatRoomCreateRes> response = ApiResponseData.of(chatRoomRes, "채팅방 생성 성공");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/group")
    public ResponseEntity<ApiResponseData<GroupChatRoomCreateRes>> createGroupChatRoom(
            @Valid @RequestBody GroupChatRoomCreateReq req,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        GroupChatRoomCreateRes chatRoomRes = chatRoomCommandService.createGroupChatRoom(req, userDetails.getUser());
        ApiResponseData<GroupChatRoomCreateRes> response = ApiResponseData.of(chatRoomRes, "채팅방 생성 성공");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseData<List<ChatRoomListAndLastChatRes>>> getChatRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ChatRoomListAndLastChatRes> chatRooms = chatRoomQueryService.getUserChatRooms(userDetails.getUser());
        ApiResponseData<List<ChatRoomListAndLastChatRes>> response = ApiResponseData.of(chatRooms, "내 채팅방 목록 조회 성공");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponseData<ChatRoomLeaveRes>> leaveChatRoom(@PathVariable Long chatRoomId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomLeaveRes chatRoomLeaveRes = chatRoomCommandService.leaveChatRoom(chatRoomId, userDetails.getUser());
        ApiResponseData<ChatRoomLeaveRes> response = ApiResponseData.of(chatRoomLeaveRes, "채팅방 나가기 성공");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseData<List<UserSearchRes>>> searchUser(
            @Valid @RequestBody UserSearchReq req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<UserSearchRes> users = userSearchService.searchUser(req, userDetails.getUser());
        ApiResponseData<List<UserSearchRes>> response = ApiResponseData.of(users, "메이트 타입별 유저 검색 성공");
        return ResponseEntity.ok(response);
    }
}
