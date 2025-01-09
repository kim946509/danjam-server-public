package com.example.danjamserver.common.dto;

import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDTO {
    private String nickname;
    private String birth;
    private Integer entryYear;
    private String major;

    public static UserInfoResponseDTO of(User user, MyProfile myProfile) {
        return UserInfoResponseDTO.builder().
                nickname(user.getNickname()).
                birth(myProfile.getBirth()).
                entryYear(myProfile.getEntryYear()).
                major(myProfile.getMajor()).
                build();
    }
}
