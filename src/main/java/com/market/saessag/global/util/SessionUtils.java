package com.market.saessag.global.util;

import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtils { // 세션 정보를 쉽게 조회할 수 있는 유틸리티 메서드

    public static SignInResponse getUserSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        SignInResponse userSession = (SignInResponse) session.getAttribute("userProfile");

        if (userSession == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return userSession;
    }

    public static void setUserSession(SignInResponse signInResponse) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        session.setAttribute("userProfile", signInResponse);
        session.setAttribute("email", signInResponse.getEmail());
    }

    public static String getUserEmail() {
        SignInResponse userSession = getUserSession();
        return userSession.getEmail();
    }
}
