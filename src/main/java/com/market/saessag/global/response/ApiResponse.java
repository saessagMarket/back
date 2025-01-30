package com.market.saessag.global.response;

import com.market.saessag.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private final String status;  // HTTP 상태 코드
    private final String message; // 응답 메시지
    private final T data;        // 실제 데이터

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return ApiResponse.<T>builder()
                .status(String.valueOf(successCode.getStatus()))
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    // 에러 응답 생성
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status(String.valueOf(errorCode.getStatus()))
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }
}