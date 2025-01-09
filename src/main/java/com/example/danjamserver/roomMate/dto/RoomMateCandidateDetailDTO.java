package com.example.danjamserver.roomMate.dto;

import com.example.danjamserver.mate.dto.BaseCandidateDetatilDTO;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RoomMateCandidateDetailDTO extends BaseCandidateDetatilDTO {

//    //User정보
//    private String nickname;
//
//    //MyProfile정보
//    private String mbti;
//    private String major;
//    private String greeting;
//    private Integer entryYear;
//    private String birth;
//    private String profileImgUrl;

    //RoomMateProfile정보
    private List<String> hopeDormitories = new ArrayList<>(); // 희망 기숙사
    private List<Integer> hopeRoomPersons = new ArrayList<>(); // 희망 방 인원
    private List<SleepHabit> sleepHabits = new ArrayList<>(); // 수면 습관
    private Boolean isSmoking; // True = 흡연자, False = 비흡연자
    private Level hotLevel; // 더위 정도
    private Level coldLevel; // 추위 정도
    private ActivityTime activityTime; // 활동 시간대
    private CleanPeriod cleanPeriod; // 청소 주기
    private ShowerTime showerTime; // 샤워 시간

}

