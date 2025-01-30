package com.market.saessag.global.util;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class TemporaryPassword {
    private final String password; // 생성된 임시 비밀번호
    private final LocalDateTime expirationTime; // 만료 시간

    public TemporaryPassword(String password, LocalDateTime expirationTime) {
        this.password = password;
        this.expirationTime = expirationTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}
