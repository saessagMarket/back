package com.market.saessag.global.interceptor;

import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 로그인 없이 접근 가능한 API 경로
        String[] allowedUrls = {
                "/api/sign-up",    // 회원가입 API
                "/api/sign-in",    // 로그인 API
                "/error"           // 에러 페이지
        };

        String requestUrl = request.getRequestURI();

        // 허용된 URL은 인터셉터를 통과
        for (String url : allowedUrls) {
            if (requestUrl.startsWith(url)) {
                return true;
            }
        }

        // 그 외의 URL은 세션 체크 후 에러 처리
        if (SessionUtil.getData("user") == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return true;
    }
}
