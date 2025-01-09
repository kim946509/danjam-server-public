package com.example.danjamserver.walkMate.dto;

import com.example.danjamserver.walkMate.domain.PreferredWalkIntensity;
import com.example.danjamserver.walkMate.domain.PreferredWalkTimeZone;
import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
////Redis에 저장할 Profile의 필요한 정보만을 담을 DTO
public class WalkMateProfileDTO {

    private Long id;
    private Long userId;
    private WalkTime preferredWalkTime;
    private Set<WalkTimeZone> preferredWalkTimeZones;
    private Set<WalkIntensity> preferredWalkIntensities;

    // WalkMateProfile을 WalkMateProfileDTO로 전환하는 메서드. cache에 꼭 필요한 데이터만 저장하기 위해 변환
    public static List<WalkMateProfileDTO> of(List<WalkMateProfile> walkMateProfiles){
        return walkMateProfiles.stream()
                .map(walkMateProfile -> WalkMateProfileDTO.builder()
                        .id(walkMateProfile.getId())
                        .userId(walkMateProfile.getUser().getId())
                        .preferredWalkTime(walkMateProfile.getPreferredWalkTime())
                        .preferredWalkTimeZones(walkMateProfile.getPreferredWalkTimeZones().stream().map(PreferredWalkTimeZone::getWalkTimeZone).collect(Collectors.toSet()))
                        .preferredWalkIntensities(walkMateProfile.getPreferredWalkIntensities().stream().map(PreferredWalkIntensity::getWalkIntensity).collect(Collectors.toSet()))
                        .build()
                ).collect(Collectors.toList());
    }
}
