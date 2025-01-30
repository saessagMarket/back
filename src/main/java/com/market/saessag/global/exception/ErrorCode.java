package com.market.saessag.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.UNAUTHORIZED, "이미 가입된 이메일입니다"),
    VERIFICATION_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 시간이 만료되었습니다"),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "잘못된 인증 코드입니다"),
    EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "이메일 인증이 필요합니다. 먼저 이메일 인증을 완료해주세요."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다"),
    EMAIL_SEND_FAILED(HttpStatus.UNAUTHORIZED, "이메일 발송에 실패했습니다"),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "유효하지 않은 이메일입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다");

    private final HttpStatus  status;
    private final String message;

}