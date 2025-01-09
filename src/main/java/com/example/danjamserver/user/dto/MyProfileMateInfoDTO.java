package com.example.danjamserver.user.dto;

import com.example.danjamserver.foodMate.dto.FoodMateProfileInputDTO;
import com.example.danjamserver.roomMate.dto.RoomMateProfileInputDTO;
import com.example.danjamserver.studyMate.dto.StudyMateProfileInputDTO;
import com.example.danjamserver.walkMate.dto.WalkMateProfileInputDTO;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileInputDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 직렬화에서 제외
public class MyProfileMateInfoDTO {
    //룸 메이트 정보
    private RoomMateProfileInputDTO roomMateProfile;
    private String roomMateStateMessage;
    //산책 메이트 정보
    private WalkMateProfileInputDTO walkMateProfile;
    private String walkMateStateMessage;
    //식사 메이트 정보
    private FoodMateProfileInputDTO foodMateProfile;
    private String foodMateStateMessage;
    //운동 메이트 정보
    private WorkoutMateProfileInputDTO workoutMateProfile;
    private String workoutMateStateMessage;
    //스터디 메이트 정보
    private StudyMateProfileInputDTO studyMateProfile;
    private String studyMateStateMessage;

}
