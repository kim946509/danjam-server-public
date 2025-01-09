package com.example.danjamserver.roomMate.dto;

import com.example.danjamserver.mate.dto.BaseFilteringDTO;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Getter
@Setter
public class RoomMateFilteringDTO extends BaseFilteringDTO {

//    String mbti;
//    String minBirthYear;
//    String maxBirthYear;
//    String minEntryYear;
//    String maxEntryYear;
//    Set<String> colleges;
    Set<String> hopeDormitories;
    Set<Integer> hopeRoomPersons;
    Set<Boolean> isSmoking;
    Level hotLevel;
    Level coldLevel;
    ActivityTime activityTime;
    CleanPeriod cleanPeriod;
    ShowerTime showerTime;
    Integer sleepHabits;

}
