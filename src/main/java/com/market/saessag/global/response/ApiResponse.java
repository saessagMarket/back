package com.market.saessag.global.response;

import com.market.saessag.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {
    private final HttpStatus status;  // HTTP 상태 코드
    private final String message; // 응답 메시지
    private final T data;        // 실제 데이터

    // 성공 응답 생성 (조회 등 데이터를 포함한 응답)
    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return ApiResponse.<T>builder()
                .status(successCode.getHttpStatus())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return ApiResponse.<T>builder()
                .status(successCode.getHttpStatus())
                .message(successCode.getMessage())
                .data(null)
                .build();
    }

    // 기본 성공 응답 (조회 등 데이터를 포함한 응답)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(SuccessCode.OK.getHttpStatus())
                .message(SuccessCode.OK.getMessage())
                .data(data)
                .build();
    }

    // 기본 성공 응답
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .status(SuccessCode.OK.getHttpStatus())
                .message(SuccessCode.OK.getMessage())
                .data(null)
                .build();
    }

    // 에러 응답 생성
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status(errorCode.getHttpStatus())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    // 기본 에러 응답
    public static <T> ApiResponse<T> error() {
        return ApiResponse.<T>builder()
                .status(ErrorCode.FAIL.getHttpStatus())
                .message(ErrorCode.FAIL.getMessage())
                .data(null)
                .build();
    }
}
