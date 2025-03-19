package com.market.saessag.domain.auth;

import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.util.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthService { // 인증과 관련된 비즈니스 로직을 처리하는 클래스

    // 세션에서 인증된 사용자의 이메일을 조회

    public String getAuthenticatedEmail() throws HttpSessionRequiredException {
        try {
            return SessionUtils.getUserSession().getEmail();
        } catch (CustomException e) {
            throw new HttpSessionRequiredException(ErrorCode.UNAUTHORIZED.getMessage());
        }
    }
}
