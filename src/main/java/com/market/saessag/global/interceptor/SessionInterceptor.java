package com.market.saessag.global.interceptor;

import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.util.PathConst;
import com.market.saessag.global.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();

        // PathConst의 허용된 URL 목록 사용
        for (String url : PathConst.EXCLUDED_PATHS) {
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
