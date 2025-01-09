package com.example.danjamserver.chatroom.dto.responses;

import com.example.danjamserver.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchRes {
    private String username;
    private String nickname;
    private String birth;
    private String major;
    private Integer gender;
    private Integer entryYear;

    @Builder
    public UserSearchRes(String username, String nickname, String birth, String major, Integer gender, Integer entryYear) {
        this.username = username;
        this.nickname = nickname;
        this.birth = birth;
        this.major = major;
        this.gender = gender;
        this.entryYear = entryYear;
    }

    public static UserSearchRes create(User user) {
        return UserSearchRes.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .birth(user.getMyProfile().getBirth())  // 생년월일
                .major(user.getMyProfile().getMajor())  // 전공
                .gender(user.getGender())  // 성별
                .entryYear(user.getMyProfile().getEntryYear())  // 입학년도
                .build();
    }
}
