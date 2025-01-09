package com.example.danjamserver.mate.service;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.mate.repository.MateProfileRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.util.exception.ForbiddenAccessException;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MateProfileService {

    private final MateProfileRepository mateProfileRepository;
    private final MyProfileRepository myProfileRepository;

    //유저가 myProfile과 해당 mate프로필을 등록했는지 확인
    public void checkUserCanFindMate(@AuthenticationPrincipal CustomUserDetails customUserDetails, String mateType) {
        try {
            MateType enumMateType = MateType.valueOf(mateType.toUpperCase());// enum으로 변환
            User user = customUserDetails.getUser();

            //유저 id로 myProfile을 찾는다. 없거나 mbti가 입력이 되지 않았으면 예외처리
            MyProfile myProfile = myProfileRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ForbiddenAccessException(ResultCode.MYPROFILE_REQUIRED));

            if(myProfile.getMbti() == null || myProfile.getMbti().isEmpty())
                throw new ForbiddenAccessException(ResultCode.MYPROFILE_REQUIRED);

            //유저 id와 mateType을 통해 프로필을 찾는다.
            mateProfileRepository.findByUserIdAndMateType(user.getId(), enumMateType)
                    .orElseThrow(() -> new ForbiddenAccessException(ResultCode.MATEPROFILE_REQUIRED));
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(ResultCode.INVALID_MATETYPE);
        }
    }
}
