package com.market.saessag.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    OK(200, "요청이 성공적으로 처리되었습니다."),
    LOGIN_SUCCESS(200, "로그인에 성공했습니다."),
    UPLOAD_SUCCESS(200, "파일 업로드에 성공했습니다.");

    private final int status;
    private final String message;
}