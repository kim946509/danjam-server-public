package com.example.danjamserver.util.exception;

import com.example.danjamserver.util.response.ApiResponseError;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // SpringSecurity 관련 예외 처리
    // AccessDeniedException 처리 : 접근 권한이 없을 때 발생하는 예외
//    @ExceptionHandler(CustomAccessDeniedException.class)
//    public ResponseEntity<ApiResponseError> handleAccessDeniedException(CustomAccessDeniedException e) {
//        return processCustomErrorResponse(e.getResultCode());
//    }

    @ExceptionHandler({SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<ApiResponseError> handleSignatureException(SignatureException e) {
        ResultCode resultCode = ResultCode.INVALID_TOKEN;
        return processCustomErrorResponse(resultCode);
    }

    /**
     * MethodArgumentNotValidException 예외 처리
     * @Valid 어노테이션을 사용하여 검증하는 과정에서 에러가 발생하면 발생하는 예외
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ResultCode resultCode = ResultCode.VALIDATION_ERROR;
        String detailedMessage = resultCode.getDetailMessage(String.join(", ", errorMessages));
        ApiResponseError response = ApiResponseError.of(resultCode, detailedMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }

    /**
     * 공통 예외 처리 메서드
     * 일반적인 예외를 처리하는 메서드
     * 외부에서 직접 호출 불가능.
     */
    private ResponseEntity<ApiResponseError> processCustomErrorResponse(ResultCode resultCode) {
        ApiResponseError response = ApiResponseError.of(resultCode);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }


    //InvalidInputException 예외 처리 : 요청에 잘못된 입력값이 들어왔을 때 발생하는 예외
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponseError> handleInvalidInputException(InvalidInputException e) {
        ResultCode resultCode = ResultCode.INVALID_INPUT;
        String detailMessage = resultCode.getDetailMessage(e.getMessage());
        ApiResponseError response = ApiResponseError.of(resultCode, detailMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }

    //InvalidTokenUser 예외 처리 : 해당 토큰에서 유저 정보를 가져올 수 없을 때 발생하는 예외
    @ExceptionHandler(InvalidTokenUser.class)
    public ResponseEntity<ApiResponseError> handleInvalidTokenUser(InvalidTokenUser e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    // MethodArgumentTypeMismatchException 예외 처리 : 쿼리파라미터 타입 불일치시 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ResultCode resultCode = ResultCode.INVALID_QUERY_PARAM;
        // todo : 배포시에는 기본 메시지만 보내도록 수정
        String baseMessage = resultCode.getDetailMessage(e.getMessage());
        String resultMessage = baseMessage;
        // Enum 타입에 맞지 않는 경우 디테일한 메시지 처리
        if (e.getRequiredType() != null && e.getRequiredType().isEnum()) {
            Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) e.getRequiredType();
            StringBuilder detailedMessage = new StringBuilder(baseMessage);
            detailedMessage.append(": '").append(e.getValue()).append("'은(는) 유효한 Enum 값이 아닙니다. ");
            detailedMessage.append("사용 가능한 값: ");

            // Enum의 허용된 값 추가
            detailedMessage.append(Arrays.toString(enumType.getEnumConstants()));

            resultMessage = detailedMessage.toString();
        }
        ApiResponseError response = ApiResponseError.of(resultCode, resultMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }

    //HTTP 요청의 메시지 바디를 읽을 수 없을 때 발생하는 예외. 예를 들어, JSON 파싱 에러나 요청 바디가 비어 있는 경우 발생.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ResultCode resultCode = ResultCode.NOT_READABLE_MESSAGE;
        // todo : 배포시에는 기본 메시지만 보내도록 수정
        String baseMessage = resultCode.getDetailMessage(e.getMessage());
        String resultMessage = baseMessage;
        // JSON 파싱 오류에 대한 세부 메시지 처리
        /**
         * enum타입에 맞지 않는 정보가 있을경우. 응답 예시
         * {
         *     "code": 40005,
         *     "message": "읽을 수 없는 데이터입니다. : JSON parse error: Cannot deserialize value of type `com.example.danjamserver.roomMate.enums.Level` from String \"많이타요\": not one of the values accepted for Enum class: [HIGH, LOW, NO_MATTER]: hotLevel "
         * }
         */
        if (e.getCause() instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) e.getCause();
            StringBuilder detailedMessage = new StringBuilder(baseMessage);
            detailedMessage.append(": ");

            jsonMappingException.getPath().forEach(reference -> {
                detailedMessage.append(reference.getFieldName()).append(" ");
            });

            resultMessage = detailedMessage.toString();
        }

        ApiResponseError response = ApiResponseError.of(resultCode, resultMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }

    //ForbiddenAccessException 예외 처리 : 해당 리소스에 대한 접근 권한이 없을 때 발생하는 예외
    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ApiResponseError> handleForbiddenAccessException(ForbiddenAccessException e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    //InvalidResourceException 예외 처리 : 해당 리소스를 찾을 수 없을 때 발생하는 예외
    @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<ApiResponseError> handleInvalidResourceException(InvalidResourceException e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    //InvalidRequestException 예외 처리 : 요청이 잘못되었을 때 발생하는 예외. ex) 이미 프로필이 존재할 때 프로필 생성 요청
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponseError> handleInvalidRequestException(InvalidRequestException e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    //CustomValidationException 예외 처리 : 서비스단에서 검사한 유효성 검사 예외 처리
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiResponseError> handleCustomValidationException(CustomValidationException e) {
        ResultCode resultCode = ResultCode.VALIDATION_ERROR;
        String detailMessage = resultCode.getDetailMessage(e.getMessage());
        ApiResponseError response = ApiResponseError.of(resultCode, detailMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }

    //FcmSubscriptionAccessException 예외 처리 : FCM 구독을 실패 했을 시 예외 처리
    @ExceptionHandler(FcmSubscriptionAccessException.class)
    public ResponseEntity<ApiResponseError> handleFcmSubScribeException(FcmSubscriptionAccessException e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    //FcmTokenNotFoundException 예외 처리 : FCM Token을 찾지 못 했을 시 예외 처리
    @ExceptionHandler(FcmTokenNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleFcmTokenException(FcmTokenNotFoundException e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    //FcmTopicNotFoundException 예외 처리 : FCM Token을 찾지 못 했을 시 예외 처리
    @ExceptionHandler(FcmTopicNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleFcmTopicException(FcmTopicNotFoundException e) {
        return processCustomErrorResponse(e.getResultCode());
    }

    //UserNotFoundException 예외 처리 : 조건에 맞는 유저를 찾지 못했을 시에
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleUserNotFoundException(UserNotFoundException e) {
        ResultCode resultCode = ResultCode.CAN_NOT_FIND_USER;
        String detailMessage = resultCode.getDetailMessage(e.getMessage());
        ApiResponseError response = ApiResponseError.of(resultCode, detailMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }

    //FileNotFoundException 예외 처리 : 조건에 맞는 유저를 찾지 못했을 시에
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponseError> handlFileNotFoundException(FileNotFoundException e) {
        ResultCode resultCode = ResultCode.FILE_UPLOAD_FAILED;
        String detailMessage = resultCode.getDetailMessage(e.getMessage());
        ApiResponseError response = ApiResponseError.of(resultCode, detailMessage);
        return ResponseEntity.status(resultCode.getStatus()).body(response);
    }
}
