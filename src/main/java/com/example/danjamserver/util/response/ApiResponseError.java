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
/**
 * 에러를 응답하기 위한 클래스
 * ApiResponseMessage와 형태는 동일하나 명확하게 하기위해 별도로 만들었음.
 */
public class ApiResponseError {

    private Integer code;
    private String message;

    public static ApiResponseError of(ResultCode resultCode) {
        return ApiResponseError.builder()
            .code(resultCode.getCode())
            .message(resultCode.getMessage())
            .build();
    }

    public static ApiResponseError of(ResultCode resultCode, String detailMessage) {
        return ApiResponseError.builder()
                .code(resultCode.getCode())
                .message(detailMessage)
                .build();
    }
}
