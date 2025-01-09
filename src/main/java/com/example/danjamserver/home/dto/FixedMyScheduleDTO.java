package com.example.danjamserver.home.dto;
import com.example.danjamserver.home.domain.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class FixedMyScheduleDTO {

    @NotNull(message = "요일을 입력해주세요.")
    private DayOfWeek dayOfWeek;
    @NotNull(message = "시작 시간을 입력해주세요.")
    private LocalTime startTime;
    @NotNull(message = "종료 시간을 입력해주세요.")
    private LocalTime endTime;

}

