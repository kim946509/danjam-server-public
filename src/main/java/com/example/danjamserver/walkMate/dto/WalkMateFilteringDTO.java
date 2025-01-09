package com.example.danjamserver.walkMate.dto;

import com.example.danjamserver.mate.dto.BaseFilteringDTO;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class WalkMateFilteringDTO extends BaseFilteringDTO {

    // todo - validation 조건 추가
//    String mbti;
//    String minBirthYear;
//    String maxBirthYear;
//    Integer minEntryYear;
//    Integer maxEntryYear;
//    List<String> colleges;
    Set<Integer> gender; // 0: 여자, 1: 남자

    // 산책 메이트 프로필 정보
    Set<WalkTimeZone> preferredWalkTimeZones; // 희망 걷기 시간대
    Set<WalkTime> preferredWalkTimes; // 희망 걷기 시간
    Set<WalkIntensity> preferredWalkIntensities; // 희망 걷기 강도

}
