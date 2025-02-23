package com.market.saessag.domain.user.controller;

import com.market.saessag.domain.user.dto.SignInRequest;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.service.SignInService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/sign-in")
    public ApiResponse<SignInResponse> signIn(
            @Validated @RequestBody SignInRequest signInRequest,
            HttpServletRequest request) {

        // 로그인 서비스 호출
        SignInResponse signInResponse = signInService.signIn(signInRequest);

        // SecurityContext 설정
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                signInResponse.getEmail(),
                null,
                Collections.emptyList()
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // 세션 설정
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        // 기존 세션 데이터 설정
        session.setAttribute("userProfile", signInResponse);
        session.setAttribute("email", signInResponse.getEmail());

        return ApiResponse.success(SuccessCode.SIGNIN_SUCCESS, signInResponse);
    }
}
