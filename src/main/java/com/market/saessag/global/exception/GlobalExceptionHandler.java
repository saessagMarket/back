package com.market.saessag.global.exception;

import com.market.saessag.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 세션 관련 예외를 먼저 처리
    @ExceptionHandler(HttpSessionRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleSessionException(HttpSessionRequiredException e) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.UNAUTHORIZED);
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getHttpStatus()).body(response);
    }

    // 2. IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.INVALID_ARGUMENT);
        return ResponseEntity.status(ErrorCode.INVALID_ARGUMENT.getHttpStatus()).body(response);
    }

    // 3. 공통된 예외 처리
    @ExceptionHandler(CustomException.class)
    public ApiResponse<String> handleCustomException(CustomException e) {
        return ApiResponse.error(e.getErrorCode());
    }

    // 4. 기타 서버 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        ApiResponse<Void> response = ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(response);
    }
}
