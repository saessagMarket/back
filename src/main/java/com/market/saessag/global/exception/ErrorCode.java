package com.market.saessag.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_EMAIL(400, "이미 가입된 이메일입니다"),
    VERIFICATION_EXPIRED(400, "인증 시간이 만료되었습니다"),
    INVALID_VERIFICATION_CODE(400, "잘못된 인증 코드입니다"),
    EMAIL_NOT_VERIFIED(400, "이메일 인증이 필요합니다. 먼저 이메일 인증을 완료해주세요."),
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다"),
    EMAIL_SEND_FAILED(500, "이메일 발송에 실패했습니다"),
    INVALID_EMAIL(400, "유효하지 않은 이메일입니다"),
    INVALID_PASSWORD(400, "현재 비밀번호가 일치하지 않습니다");

    private final int status;
    private final String message;

}