package com.market.saessag.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS = 5; //최대 요청 횟수
    private static final long TIME_WINDOW = 60000; // 1분
    private final Map<String, RequestData> requestCounts = new ConcurrentHashMap<>();

    static class RequestData {
        int count;
        long timeStamp;

        RequestData(int count, long timeStamp) {
            this.count = count;
            this.timeStamp = timeStamp;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr(); // 요청 IP 주소
        String path = request.getRequestURI(); // 요청 URI
        String key = ip + ":" + path;

        long now = Instant.now().toEpochMilli(); // 요청이 온 시간

        requestCounts.compute(key, (k, data) -> {
            if (data == null || now - data.timeStamp > TIME_WINDOW) {
                return new RequestData(1, now);
            } else if (data.count < MAX_REQUESTS) {
                data.count++;
                return data;
            } else {
                return data;
            }
        });

        if (requestCounts.get(key).count >= MAX_REQUESTS) {
            response.setStatus(429);
            return false;
        }
        return true;
    }
}