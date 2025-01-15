package com.market.saessag.domain.email.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationRequest { // 이메일 인증 코드 확인 DTO
    private String email; // 인증할 이메일 주소
    private String code; // 사용자가 입력한 인증 코드

}
