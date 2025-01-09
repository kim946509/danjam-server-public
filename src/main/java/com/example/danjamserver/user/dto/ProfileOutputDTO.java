package com.example.danjamserver.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileOutputDTO {
    private String domitoryOptions;
    private String roomPersonOptions;
    private Boolean smoking;
    private Integer hotLevel;
    private Integer coldLevel;
    private Integer activityTime;
    private String cleanPeriod;
    private String showerTime;
    private String sleepHabitOptions;
    private String mbti;
    private String greeting;
}