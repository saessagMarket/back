package com.market.saessag.global.config;

// 인증 경로 공통 상수 클래스
public class PathConst {
    // 인증(로그인)이 필요없는 경로들
    public static final String[] EXCLUDED_PATHS = {
            "/api/sign-up/**",                 // 회원가입 관련
            "/api/sign-in",                    // 로그인
            "/api/products/list",              // 상품 목록 조회
            "/api/password/find",              // 비밀번호 발급
            "/error",                           // 에러 페이지
            "/ws/**",                           // 웹 소켓
            "/topic/**",
            "/api/chat/**" // 채팅 관련 (임시)
    };

    // 인증(로그인)이 필요한 경로들
    public static final String[] AUTHENTICATED_PATHS = {
            "/api/products",                    // 상품 등록
            "/api/products/**",                 // 상품 수정, 삭제 등
            "/api/photos/**",                   // 사진 관련
            "/api/profile/**",                  // 프로필 사진 관련
            "/api/password/change"              // 비밀번호 변경
    };

    private PathConst() {} // 인스턴스화 방지
}
