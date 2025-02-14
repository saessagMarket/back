package com.market.saessag.domain.user.controller;

import com.market.saessag.domain.user.dto.SignInRequest;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.domain.user.service.SignInService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignInController {

    private final SignInService signInService;
    private final UserRepository userRepository;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(
            @Validated @RequestBody SignInRequest signInRequest,
            HttpServletRequest request) {

        // 기존 로그인 로직 수행
        SignInResponse userProfileResponse = signInService.signIn(signInRequest);

        // 사용자 조회
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 로그인 성공 시 SecurityContext에 인증 정보가 저장됨
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities() // User 엔티티에 getAuthorities() 메서드 필요
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // 세션에 SecurityContext와 사용자 정보 저장
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        // 기존 세션 데이터 설정
        session.setAttribute("userProfile", userProfileResponse);
        session.setAttribute("email", user.getEmail());

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, userProfileResponse));
    }
}
