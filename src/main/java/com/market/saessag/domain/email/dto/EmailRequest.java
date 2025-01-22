package com.market.saessag.domain.email.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailRequest {
    private String email; // 인증을 요청할 이메일 주소

}
