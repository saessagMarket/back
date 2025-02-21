package com.market.saessag.global.interceptor;

import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String ip = request.getRemoteAddr(); // 요청 IP 주소
        String path = request.getRequestURI(); // 요청 URI
        String key = ip + ":" + path;

        long now = Instant.now().toEpochMilli(); // 요청이 온 시간

        requestCounts.compute(key, (k, data) -> {
            if (data == null || now - data.timeStamp > TIME_WINDOW) {
                return new RequestData(0, now);
            }
            return data;
        });


        if (requestCounts.get(key).count >= MAX_REQUESTS) {
            throw new CustomException(ErrorCode.RATE_LIMIT);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String ip = request.getRemoteAddr(); // 요청 IP 주소
        String path = request.getRequestURI(); // 요청 URI
        String key = ip + ":" + path;

        if (response.getStatus() >= 400) {
            requestCounts.computeIfPresent(key, (k, data) -> {
                data.count++;
                return data;
            });
        }
    }
}