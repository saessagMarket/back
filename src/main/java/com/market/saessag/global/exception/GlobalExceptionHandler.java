package com.market.saessag.global.exception;

import com.market.saessag.global.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 세션 관련 예외를 먼저 처리
    @ExceptionHandler(HttpSessionRequiredException.class)
    public ResponseEntity<ErrorResponse> handleSessionException(HttpSessionRequiredException e) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // 2. IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_ARGUMENT);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // 3. 공통된 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // 4. 기타 서버 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}