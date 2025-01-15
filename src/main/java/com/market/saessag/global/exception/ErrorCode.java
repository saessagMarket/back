package com.market.saessag.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_EMAIL(400, "이미 가입된 이메일입니다"),
    VERIFICATION_EXPIRED(400, "인증 시간이 만료되었습니다"),
    INVALID_VERIFICATION_CODE(400, "잘못된 인증 코드입니다"),
    EMAIL_NOT_VERIFIED(400, "이메일 인증이 필요합니다. 먼저 이메일 인증을 완료해주세요.");

    private final int status;
    private final String message;

}