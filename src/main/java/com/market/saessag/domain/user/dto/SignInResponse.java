// 로그인
package com.market.saessag.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private final Long id;
    private final String email;
    private final String profileUrl;
    private final String nickname;

    @Builder
    public SignInResponse(Long id, String email, String profileUrl, String nickname){
        this.id = id;
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
    }

}
