package com.example.danjamserver.workoutMate.dto;

import com.example.danjamserver.mate.dto.BaseCandidateDetatilDTO;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import com.example.danjamserver.workoutMate.enums.WorkoutTimeZone;
import com.example.danjamserver.workoutMate.enums.WorkoutType;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class WorkoutMateCandiateDetailDTO extends BaseCandidateDetatilDTO {

    // User정보
    private Integer gender;

    //WorkoutMateProfile정보
    private Set<WorkoutTimeZone> preferredWorkoutTimeZones; // 희망 운동 시간대

    private WorkoutIntensity preferredWorkoutIntensity; // 희망 운동 강도

    private Set<WorkoutType> preferredWorkoutTypes; // 희망 운동 종류

    private Set<WorkoutTime> preferredWorkoutTimes; // 희망 운동 소요 시간

}
