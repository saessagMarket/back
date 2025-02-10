package com.market.saessag.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    UPLOAD_SUCCESS(HttpStatus.OK, "파일 업로드에 성공했습니다."),
    GET_PROFILE_SUCCESS(HttpStatus.OK, "프로필 이미지 조회에 성공했습니다."),
    PRODUCT_CREATED(HttpStatus.CREATED, "상품이 성공적으로 생성되었습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "리소스가 성공적으로 삭제되었습니다."),
    PRODUCT_UPDATED(HttpStatus.OK, "상품이 성공적으로 수정되었습니다."),
    LIKE_ADDED(HttpStatus.OK, "좋아요가 추가되었습니다."),
    LIKE_REMOVED(HttpStatus.OK, "좋아요가 취소되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
