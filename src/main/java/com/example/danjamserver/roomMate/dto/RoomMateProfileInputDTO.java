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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomMateProfileInputDTO {

    @NotEmpty(message = "기숙사 옵션을 선택해주세요.")
    @Size(min = 1, max = 20, message = "기숙사 옵션은 1개 이상 20개 이하로 선택해주세요.")
    private Set<String> hopeDormitories; // 희망 기숙사

    @NotEmpty(message = "방 옵션을 선택해주세요.")
    @Size(min = 1, max = 20, message = "방 인실은 1개 이상 20개 이하로 선택해주세요.")
    private Set<Integer> hopeRoomPersons; // 희망 방 인원

    @NotEmpty(message = "수면 습관을 선택해주세요.")
    @Size(min = 1, max = 5, message = "수면 습관은 1개 이상 5개 이하로 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private Set<SleepHabit> sleepHabits; // 수면 습관

    @NotNull(message = "흡연 여부를 선택해주세요.")
    private Boolean isSmoking; // True = 흡연자, False = 비흡연자

    @NotNull(message = "더위 정도를 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private Level hotLevel; //

    @NotNull(message = "추위 정도를 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private Level coldLevel; //

    @NotNull(message = "활동 시간대를 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private ActivityTime activityTime;

    @NotNull(message = "청소 주기를 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private CleanPeriod cleanPeriod;

    @NotNull(message = "샤워 시간을 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private ShowerTime showerTime;

    //정적 팩토리 메소드
    public static RoomMateProfileInputDTO from(RoomMateProfile roomMateProfile) {
        RoomMateProfileInputDTO profile = new RoomMateProfileInputDTO();

        profile.setSleepHabits(roomMateProfile.getOwnSleepHabits().stream()
                .map(OwnSleepHabit::getSleepHabit).collect(Collectors.toSet()));
        profile.setHopeDormitories(roomMateProfile.getHopeDormitories().stream()
                .map(HopeDormitory::getHopeDormitory).collect(Collectors.toSet()));
        profile.setHopeRoomPersons(roomMateProfile.getHopeRoomPersons().stream()
                .map(HopeRoomPerson::getHopeRoomPerson).collect(Collectors.toSet()));
        profile.setIsSmoking(roomMateProfile.getIsSmoking());
        profile.setHotLevel(roomMateProfile.getHotLevel());
        profile.setColdLevel(roomMateProfile.getColdLevel());
        profile.setActivityTime(roomMateProfile.getActivityTime());
        profile.setCleanPeriod(roomMateProfile.getCleanPeriod());
        profile.setShowerTime(roomMateProfile.getShowerTime());
        return profile;
    }
}
