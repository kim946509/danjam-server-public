package com.example.danjamserver.notice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FcmTopicSendDto {
    private final String topic;
    private final String title;
    private final String body;
    private String platform;
}
