package com.example.danjamserver.roomMate.service;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.example.danjamserver.roomMate.dto.RoomMateCandidateDetailDTO;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("RoomMateCandidateService 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomMateCandidateServiceTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private CustomUserDetails customUserDetails;

    @Autowired
    private RoomMateCandidateService roomMateCandidateService;


    // 유저 데이터를 반환하는 메서드 예시
    private User getUser() {
        User user = userRepository.findByUsername("test0004").get();
        return user;
    }
}