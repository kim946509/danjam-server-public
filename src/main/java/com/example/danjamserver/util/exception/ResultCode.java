package com.example.danjamserver.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

    /**
     * 성공 0번대
     */
    SUCCESS(HttpStatus.OK, 0, "성공적으로 처리되었습니다."),


    /**
     * VALIDATION 관련 100번대
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 100, "잘못된 입력값이 존재합니다."), //DTO의 Validation을 통과 못할시 사용되는 예외 코드


    /**
     * 400번대
     */
    //유효하지 않은(잘못된) 입력값(40000 ~ 40099번대)
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 40000, "잘못된 값이 존재합니다."),
    INVALID_QUERY_PARAM(HttpStatus.BAD_REQUEST, 40001, "쿼리 파라미터 타입이 일치하지 않습니다."), // 쿼리 파라미터 타입이 일치하지 않을 때 사용되는 예외 코드
    NULL_INPUT_VALUE(HttpStatus.BAD_REQUEST, 40002, "입력값이 없는 항목이 있습니다."),
    INVALID_SCHEDULE_DATE(HttpStatus.BAD_REQUEST, 40003, "시작일자가 종료일자보다 늦을 수 없습니다."),
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, 40004, "시작시간이 종료시간보다 늦을 수 없습니다."),
    NOT_READABLE_MESSAGE(HttpStatus.BAD_REQUEST, 40005, "읽을 수 없는 데이터입니다."),
    INVALID_MATETYPE(HttpStatus.BAD_REQUEST, 40006, "잘못된 mateType입니다."),

    //유효하지 않은 리소스(40100 ~ 40199번대
    CAN_NOT_FIND_RESOURCE(HttpStatus.BAD_REQUEST, 40100, "해당 리소스를 찾을 수 없습니다."),
    CAN_NOT_FIND_USER_PROFILE(HttpStatus.BAD_REQUEST, 40101, "프로필을 찾을 수 없습니다."),
    CAN_NOT_FIND_SCHOOL(HttpStatus.BAD_REQUEST, 40102, "학교를 찾을 수 없습니다."),
    CAN_NOT_FIND_MAJOR(HttpStatus.BAD_REQUEST, 40103, "학과를 찾을 수 없습니다."),
    CAN_NOT_FOUND_CHATROOM(HttpStatus.BAD_REQUEST, 40104, "채팅방을 찾을 수 없습니다."),
    CAN_NOT_FIND_USER(HttpStatus.BAD_REQUEST, 40105, "유저를 찾을 수 없습니다."),
    CAN_NOT_FIND_REPORT(HttpStatus.BAD_REQUEST, 40106, "신고내역이 존재하지 않습니다."),
    CAN_NOT_FIND_NOTIFICATION(HttpStatus.BAD_REQUEST, 40107, "알림내역이 존재하지 않습니다."),
    ALREADY_EXIST_NOTIFICATION(HttpStatus.BAD_REQUEST, 40108, "이미 처리가 된 알림 내역입니다."),

    //보안 관련(40200 ~ 40299번대)
    REQUIRED_LOGIN(HttpStatus.UNAUTHORIZED, 40200 , "로그인이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40201, "유효하지 않은 토큰입니다."),//토큰의 유저 정보가 존재하지 않을 때 사용되는 예외 코드
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 40202, "토큰이 만료되었습니다."), //토큰이 만료되었을 때 사용되는 예외 코드
    CAN_NOT_ACCESS_RESOURCE(HttpStatus.FORBIDDEN, 40203, "해당 리소스에 대한 접근 권한이 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 40204, "접근 권한이 없습니다."), //접근 권한이 없을 때 사용되는 예외 코드
    MATEPROFILE_REQUIRED(HttpStatus.FORBIDDEN, 40206, "프로필을 먼저 작성해야 합니다."), //프로필을 작성해야 하는 경우 사용되는 예외 코드
    MYPROFILE_REQUIRED(HttpStatus.FORBIDDEN, 40205, "내 정보를 먼저 작성해야 합니다."), //프로필을 작성해야 하는 경우 사용되는 예외 코드

    //리소스 중복(40300 ~ 40399번대)
    ALREADY_EXIST_PROFILE(HttpStatus.BAD_REQUEST, 40300, "이미 프로필이 존재합니다."),
    ALREADY_EXIST_REPORT(HttpStatus.BAD_REQUEST, 40301, "신고 내역이 존재합니다."),

    // 유저 관련
    NO_MATCHING_USER_FOUND(HttpStatus.NOT_FOUND, 40022, "해당 조건에 맞는 사용자를 찾을 수 없습니다."),
    NOT_EQUAL_GENDER(HttpStatus.NOT_ACCEPTABLE, 40023, "같은 성별이 포함되어있습니다."),

    /**
     * 500번대
      */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500,"예기치 못한 서버 오류가 발생했습니다."),
    FCM_SUBSCRIPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "FCM TOPIC 구독에 실패하였습니다."),
    FCM_NO_MATCHING_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, 500, "해당 사용자의 FCM TOKEN을 찾을 수 없습니다."),
    FCM_NO_MATCHING_TOPIC(HttpStatus.INTERNAL_SERVER_ERROR, 500, "해당 FCM TOPIC을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, "파일 업로드를 실패했습니다.."),

    /**
     * SSE Emitter Error
     */
    SSE_EMITTER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "SSE Emitter 처리 중 에러가 발생했습니다."),

    /**
     * RabbitMq Exception
     */
    RabbitMq_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "RabbitMq 처리 중 에러가 발생했습니다.");

    private final HttpStatus status;
    private final Integer code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public String getDetailMessage(String message){
        return this.getMessage() + " : " + message;
    }
}