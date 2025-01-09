package com.example.danjamserver.workoutMate.dto;

import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTime;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTimeZone;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutType;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutMateProfileDTO {

    private Long id;
    private Long userId;
    private WorkoutIntensity preferredWorkoutIntensity;
    private Set<WorkoutType> preferredWorkoutTypes;
    private Set<WorkoutTimeZone> preferredWorkoutTimeZones;
    private Set<WorkoutTime> preferredWorkoutTimes;

    // 필터링 진행후 결과인 List<WorkoutMateProfile>을 List<WorkoutMateProfileDTO>로 변환. 변환 이후 redis 캐시에 저장
    public static List<WorkoutMateProfileDTO> of(List<WorkoutMateProfile> workoutMateProfiles) {
        return workoutMateProfiles.stream()
                .map(WorkoutMateProfile-> WorkoutMateProfileDTO.builder()
                        .id(WorkoutMateProfile.getId())
                        .userId(WorkoutMateProfile.getUser().getId())
                        .preferredWorkoutIntensity(WorkoutMateProfile.getPreferredWorkoutIntensity())
                        .preferredWorkoutTypes(WorkoutMateProfile.getPreferredWorkoutTypes().stream().map(PreferredWorkoutType::getWorkoutType).collect(Collectors.toSet()))
                        .preferredWorkoutTimeZones(WorkoutMateProfile.getPreferredWorkoutTimeZones().stream().map(PreferredWorkoutTimeZone::getWorkoutTimeZone).collect(Collectors.toSet()))
                        .preferredWorkoutTimes(WorkoutMateProfile.getPreferredWorkoutTimes().stream().map(PreferredWorkoutTime::getWorkoutTime).collect(Collectors.toSet()))
                        .build()
                ).collect(Collectors.toList());
    }
}
