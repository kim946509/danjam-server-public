package com.example.danjamserver.workoutMate.dto;

import com.example.danjamserver.mate.dto.BaseFilteringDTO;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@Getter @Setter
@SuperBuilder
public class WorkoutMateFilteringDTO extends BaseFilteringDTO {

// BaseFilteringDTO
//    String mbti;
//    String minBirthYear;
//    String maxBirthYear;
//    String minEntryYear;
//    String maxEntryYear;
//    Set<String> colleges;

    Set<Integer> gender;
    // 운동 메이트 프로필 정보
    Set<WorkoutTimeZone> preferredWorkoutTimeZones; // 희망 운동 시간대
    Set<WorkoutIntensity> preferredWorkoutIntensities; // 희망 운동 강도
    Set<WorkoutType> preferredWorkoutTypes; // 희망 운동 종류
    Set<WorkoutTime> preferredWorkoutTimes; // 희망 운동 소요 시간

}
