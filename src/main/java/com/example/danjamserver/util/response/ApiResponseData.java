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
public class ApiResponseData<T> {

    private final Integer code = ResultCode.SUCCESS.getCode();
    private String message = ResultCode.SUCCESS.getMessage(); // default message
    private T data;

    public static <T> ApiResponseData<T> of(T data, String message) {
        return ApiResponseData.<T>builder()
                .message(message)
                .data(data)
                .build();
    }
}
