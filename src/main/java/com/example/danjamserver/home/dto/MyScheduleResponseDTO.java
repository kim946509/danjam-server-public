package com.example.danjamserver.home.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyScheduleResponseDTO {
    private Long id;
    private String title;
    private String memo;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
