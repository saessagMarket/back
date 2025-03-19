package com.market.saessag.domain.user.controller;

import com.market.saessag.domain.user.dto.SignInRequest;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.service.SignInService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import com.market.saessag.global.util.SessionUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/sign-in")
    public ApiResponse<SignInResponse> signIn(
            @Validated @RequestBody SignInRequest signInRequest) {

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
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        // 기존 세션 데이터 설정
        SessionUtils.setUserSession(signInResponse);

        return ApiResponse.success(SuccessCode.SIGNIN_SUCCESS, signInResponse);
    }
}
