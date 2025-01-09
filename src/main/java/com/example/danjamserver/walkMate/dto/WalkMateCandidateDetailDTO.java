package com.example.danjamserver.walkMate.dto;

import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WalkMateCandidateDetailDTO {

    //User정보
    private String nickname;
    private Integer gender;

    //MyProfile정보
    private String mbti;
    private String major;
    private String greeting;
    private Integer entryYear;
    private String birth;
    private String profileImgUrl;

    //WalkMateProfile정보
    private Set<WalkTimeZone> preferredWalkTimeZones; // 희망 걷기 시간대
    private WalkTime preferredWalkTime; // 희망 걷기 시간
    private Set<WalkIntensity> preferredWalkIntensities; // 희망 걷기 강도

}
