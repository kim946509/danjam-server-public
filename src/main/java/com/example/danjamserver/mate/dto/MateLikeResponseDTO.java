package com.example.danjamserver.mate.dto;


import com.example.danjamserver.mate.domain.MateLike;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.user.domain.User;
import lombok.Data;

@Data
public class MateLikeResponseDTO {
    private MateType mateType;
    private String nickname; //닉네임
    private String birth; //생년월일
    private Integer entryYear; //학번
    private String major; //전공
    private String greeting; //소개글

    public static MateLikeResponseDTO from(MateLike mateLike) {
        User targetUser = mateLike.getTargetUser();
        MateLikeResponseDTO resultLike = new MateLikeResponseDTO();

        resultLike.setMateType(mateLike.getMateType());
        resultLike.setNickname(targetUser.getNickname());
        resultLike.setBirth(targetUser.getMyProfile().getBirth());
        resultLike.setEntryYear(targetUser.getMyProfile().getEntryYear());
        resultLike.setMajor(targetUser.getMyProfile().getMajor());
        resultLike.setGreeting(targetUser.getMyProfile().getGreeting());
        return resultLike;
    }
}
