package com.market.saessag.global.interceptor;

import com.market.saessag.global.config.PathConst;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.util.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();

        // PathConst의 허용된 URL 목록 사용
        for (String url : PathConst.EXCLUDED_PATHS) {
            if (requestUrl.startsWith(url)) {
                return true;
            }
        }

        // 세션 정보 조회
        try {
            SessionUtils.getUserSession();
        } catch (CustomException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }

        return true;
    }
}
