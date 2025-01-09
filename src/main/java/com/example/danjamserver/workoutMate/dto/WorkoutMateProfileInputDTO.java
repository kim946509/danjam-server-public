package com.example.danjamserver.workoutMate.dto;

import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTime;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutTimeZone;
import com.example.danjamserver.workoutMate.domain.PreferredWorkoutType;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutMateProfileInputDTO {

    @NotEmpty(message = "희망 운동 시간대를 선택해주세요.")
    private Set<WorkoutTimeZone> preferredWorkoutTimeZones;

    @NotNull(message = "희망 운동 강도를 선택해주세요.")
    private WorkoutIntensity preferredWorkoutIntensity;

    @NotEmpty(message = "희망 운동 종류를 선택해주세요.")
    private Set<WorkoutType> preferredWorkoutTypes;

    @NotEmpty(message = "희망 운동 소요 시간을 선택해주세요.")
    private Set<WorkoutTime> preferredWorkoutTimes;

    public static WorkoutMateProfileInputDTO from(WorkoutMateProfile workoutMateProfile) {
        WorkoutMateProfileInputDTO profile = new WorkoutMateProfileInputDTO();

        profile.setPreferredWorkoutTimeZones(workoutMateProfile.getPreferredWorkoutTimeZones().stream()
                .map(PreferredWorkoutTimeZone::getWorkoutTimeZone)
                .collect(Collectors.toSet()));

        profile.setPreferredWorkoutIntensity(workoutMateProfile.getPreferredWorkoutIntensity());
        profile.setPreferredWorkoutTypes(workoutMateProfile.getPreferredWorkoutTypes().stream()
                .map(PreferredWorkoutType::getWorkoutType)
                .collect(Collectors.toSet()));

        profile.setPreferredWorkoutTimes(workoutMateProfile.getPreferredWorkoutTimes().stream()
                .map(PreferredWorkoutTime::getWorkoutTime)
                .collect(Collectors.toSet()));

        return profile;
    }
}
