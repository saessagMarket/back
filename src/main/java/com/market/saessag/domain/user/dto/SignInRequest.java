// 로그인
package com.market.saessag.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInRequest {
    private final String email;
    private final String password;

    @Builder
    public SignInRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

}
