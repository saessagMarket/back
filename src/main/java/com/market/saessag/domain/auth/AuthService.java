package com.market.saessag.domain.auth;

import com.market.saessag.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthService {

    // 세션에서 인증된 사용자의 이메일을 조회
    public String getAuthenticatedEmail() throws HttpSessionRequiredException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        String email = (String) session.getAttribute("email");
        if (email == null) {
            throw new HttpSessionRequiredException(ErrorCode.UNAUTHORIZED.getMessage());
        }
        return email;
    }
}
