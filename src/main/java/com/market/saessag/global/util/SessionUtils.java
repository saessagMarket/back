package com.market.saessag.global.util;

import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtils {

    public static SignInResponse getUserSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        SignInResponse userSession = (SignInResponse) session.getAttribute("userProfile");

        if (userSession == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return userSession;
    }
}
