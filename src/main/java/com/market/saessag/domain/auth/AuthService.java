package com.market.saessag.domain.auth;

import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService { // 인증과 관련된 비즈니스 로직을 처리하는 클래스

    // 세션에서 인증된 사용자의 이메일을 조회

    public String getLoggedInUserEmail() {
        try {
            // 세션에서 이메일 가져오기
            return SessionUtils.getUserSession().getEmail();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
