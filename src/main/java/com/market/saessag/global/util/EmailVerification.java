package com.market.saessag.global.util;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EmailVerification {
    private final String code; // 생성된 인증 코드
    private final LocalDateTime expirationTime; // 만료 시간
    private boolean verified; // 인증 완료 여부

    public EmailVerification(String code, LocalDateTime expirationTime) {
        this.code = code;
        this.expirationTime = expirationTime;
        this.verified = false;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public void verify() {
        this.verified = true;
    }

}