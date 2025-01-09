package com.example.danjamserver.springSecurity.service;

import com.example.danjamserver.springSecurity.dto.UserBasicInformation;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBasicInformationService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserBasicInformation getUserBasicInformation(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserBasicInformation(user.getNickname(), user.getGender(), user.getRole().name(), user.getSchool().getKorName(), user.getMyProfile().getMajor());
    }
}
