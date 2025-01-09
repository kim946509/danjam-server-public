package com.example.danjamserver.roomMate.dto;

import com.example.danjamserver.roomMate.domain.HopeDormitory;
import com.example.danjamserver.roomMate.domain.HopeRoomPerson;
import com.example.danjamserver.roomMate.domain.OwnSleepHabit;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.roomMate.enums.SleepHabit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Redis에 저장할 RoomMateProfile 정보를 담을 DTO
public class RoomMateProfileDTO {
    private Long id;
    private Long userId;
    private Boolean isSmoking;
    private Level hotLevel;
    private Level coldLevel;
    private ActivityTime activityTime;
    private CleanPeriod cleanPeriod;
    private ShowerTime showerTime;
    private List<String> hopeDormitories;
    private List<Integer> hopeRoomPersons;
    private List<SleepHabit> ownSleepHabits;

    // RoomMateProfile Entity를 RoomMateProfileDTO로 변환하는 메서드
    public static List<RoomMateProfileDTO> of(List<RoomMateProfile> profilesByFilters) {
        return profilesByFilters.stream()
                .map(profile -> RoomMateProfileDTO.builder()
                        .id(profile.getId())
                        .userId(profile.getUser().getId())
                        .isSmoking(profile.getIsSmoking())
                        .hotLevel(profile.getHotLevel())
                        .coldLevel(profile.getColdLevel())
                        .activityTime(profile.getActivityTime())
                        .cleanPeriod(profile.getCleanPeriod())
                        .showerTime(profile.getShowerTime())
                        .hopeDormitories(profile.getHopeDormitories().stream().map(HopeDormitory::getHopeDormitory).collect(Collectors.toList()))
                        .hopeRoomPersons(profile.getHopeRoomPersons().stream().map(HopeRoomPerson::getHopeRoomPerson).collect(Collectors.toList()))
                        .ownSleepHabits(profile.getOwnSleepHabits().stream().map(OwnSleepHabit::getSleepHabit).collect(Collectors.toList()))
                        .build())
                .toList();
    }
}
