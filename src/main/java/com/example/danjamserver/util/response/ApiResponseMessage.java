package com.example.danjamserver.util.response;

import com.example.danjamserver.util.exception.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseMessage {

    private final Integer code = ResultCode.SUCCESS.getCode();
    private String message = ResultCode.SUCCESS.getMessage(); // default message

    public static ApiResponseMessage of(String message) {
        return ApiResponseMessage.builder()
                .message(message)
                .build();
    }

}
