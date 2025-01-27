package com.market.saessag.global.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionUtil {
    // 세션 가져오기
    public static HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest().getSession();
    }

    // 세션에 데이터 저장
    public static void setData(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    // 세션에서 데이터 가져오기
    public static Object getData(String key) {
        return getSession().getAttribute(key);
    }

    // 특정 세션 삭제
    public static void removeData(String key) {
        getSession().removeAttribute(key);
    }

    // 모든 세션 삭제
    public static void clear() {
        getSession().invalidate();
    }
}

