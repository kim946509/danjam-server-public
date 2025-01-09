package com.example.danjamserver.util.response;

import com.example.danjamserver.util.exception.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 추후 ApiResponseMessage와 ApiResponseData로 모두 변경된 이후에 삭제할 예정.
public class RestResponse<T> implements Serializable {

    private HttpStatus status;
    private Integer code;
    private String message;
    private T data;

    public static <T> RestResponse<T> success(T data) {
        return RestResponse.<T>builder()
            .status(ResultCode.SUCCESS.getStatus())
            .code(ResultCode.SUCCESS.getCode())
            .message(ResultCode.SUCCESS.getMessage())
            .data(data)
            .build();
    }

    public static <T> RestResponse<T> error(ResultCode resultCode, T data) {
        return RestResponse.<T>builder()
            .status(resultCode.getStatus())
            .code(resultCode.getCode())
            .message(resultCode.getMessage())
            .data(data)
            .build();
    }

    public static <T> RestResponse<T> error(ResultCode resultCode) {
        return RestResponse.<T>builder()
            .status(resultCode.getStatus())
            .code(resultCode.getCode())
            .message(resultCode.getMessage())
            .build();
    }
}