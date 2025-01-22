package com.market.saessag.domain.email.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordChangeRequest {
    private String email;
    private String currentPassword;
    private String newPassword; // 새로 설정할 비밀번호
}
