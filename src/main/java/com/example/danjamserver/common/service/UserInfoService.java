package com.example.danjamserver.common.service;

import com.example.danjamserver.common.dto.UserInfoResponseDTO;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.UserNotFoundException;
import com.example.danjamserver.util.response.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository userRepository;
    private final MyProfileRepository myProfileRepository;

    public RestResponse<UserInfoResponseDTO> getUserInfo(String username) {

        // 유저가 없다면 40201 에러 발생
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("해당 이름의 유저가 존재하지 않습니다."));
        MyProfile myProfile = myProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new UserNotFoundException("해당 이름의 유저 프로필이 존재하지 않습니다."));

        return RestResponse.success(UserInfoResponseDTO.of(user, myProfile));
    }

    public RestResponse<String> getUserNickname(CustomUserDetails customUserDetails) {
        if(customUserDetails == null) throw new InvalidTokenUser();

        String username = customUserDetails.getUsername();

        // 유저가 없다면 40201 에러 발생
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("해당 이름의 유저가 존재하지 않습니다."));
        return RestResponse.success(user.getNickname());
    }
}
