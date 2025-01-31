package com.market.saessag.global.config;

// 인증 경로 공통 상수 클래스
public class PathConst {
    // 인증(로그인)이 필요없는 경로들
    public static final String[] EXCLUDED_PATHS = {
            "/api/sign-up",                    // 회원가입
            "/api/sign-in",                    // 로그인
            "/api/products",                   // 상품 목록 조회
            "/api/products/{id}",              // 상품 상세 조회
            "/error"                           // 에러 페이지
    };

    // 인증(로그인)이 필요한 경로들
    public static final String[] AUTHENTICATED_PATHS = {
            "/api/products/create",            // 상품 등록
            "/api/products/{id}/update",       // 상품 수정
            "/api/products/{id}/delete",       // 상품 삭제
            "/api/profile/upload-image",        // 프로필 사진 업로드
            "/api/profile"                     // 프로필 사진 조회
    };

    private PathConst() {} // 인스턴스화 방지
}
