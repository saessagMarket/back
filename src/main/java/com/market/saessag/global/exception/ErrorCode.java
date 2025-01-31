package com.market.saessag.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다"),
    VERIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다"),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다"),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다. 먼저 이메일 인증을 완료해주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다"),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "프로필 사진이 없습니다"),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 요청 값입니다");

    private final HttpStatus status;
    private final String message;
}
