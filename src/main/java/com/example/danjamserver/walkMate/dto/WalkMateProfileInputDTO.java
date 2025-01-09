package com.example.danjamserver.walkMate.dto;


import com.example.danjamserver.walkMate.domain.PreferredWalkIntensity;
import com.example.danjamserver.walkMate.domain.PreferredWalkTimeZone;
import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.enums.WalkIntensity;
import com.example.danjamserver.walkMate.enums.WalkTime;
import com.example.danjamserver.walkMate.enums.WalkTimeZone;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkMateProfileInputDTO {

    @NotEmpty(message = "희망 걷기 시간대를 선택해주세요.")
    private Set<WalkTimeZone> preferredWalkTimeZones;

    @NotNull(message = "희망 걷기 소요 시간을 선택해주세요.")
    private WalkTime preferredWalkTime;

    @NotEmpty(message = "희망 걷기 강도를 선택해주세요.")
    private Set<WalkIntensity> preferredWalkIntensities;

    // 정적 팩토리 메서드
    public static WalkMateProfileInputDTO from(WalkMateProfile walkMateProfile) {
        WalkMateProfileInputDTO profile = new WalkMateProfileInputDTO();

        // Enum으로 변환하여 설정
        profile.setPreferredWalkTimeZones(walkMateProfile.getPreferredWalkTimeZones().stream()
                .map(PreferredWalkTimeZone::getWalkTimeZone)
                .collect(Collectors.toSet()));

        profile.setPreferredWalkTime(walkMateProfile.getPreferredWalkTime());

        profile.setPreferredWalkIntensities(walkMateProfile.getPreferredWalkIntensities().stream()
                .map(PreferredWalkIntensity::getWalkIntensity)
                .collect(Collectors.toSet()));
        return profile;
    }

}
