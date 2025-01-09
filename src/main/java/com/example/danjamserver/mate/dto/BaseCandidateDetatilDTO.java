package com.example.danjamserver.mate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@SuperBuilder
public abstract class BaseCandidateDetatilDTO {

    //User정보
    private String nickname;

    //MyProfile정보
    private String mbti;
    private String major;
    private String greeting;
    private Integer entryYear;
    private String birth; // todo - 앞자리 6자리 주는건 민감한 정보이지 않을까? 앞에 2자리만 주는게 좋을듯
    private String profileImgUrl;

}
