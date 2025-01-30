package com.market.saessag.global.util;

// 제외할 경로 공통 상수 클래스
public class PathConst {
    public static final String[] EXCLUDED_PATHS = {
            "/api/sign-up",
            "/api/sign-in",
            "/error"
    };

    private PathConst() {} // 인스턴스화 방지
}
