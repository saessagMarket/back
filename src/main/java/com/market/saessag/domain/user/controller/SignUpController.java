package com.market.saessag.domain.user.controller;

import com.market.saessag.domain.email.dto.EmailRequest;
import com.market.saessag.domain.email.dto.EmailVerificationRequest;
import com.market.saessag.domain.email.service.EmailService;
import com.market.saessag.domain.user.service.SignUpService;
import com.market.saessag.domain.user.dto.SignUpRequest;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign-up")
public class SignUpController {

    private final SignUpService signUpService;
    private final EmailService emailService;

    // 이메일 중복 확인 및 인증 코드 발송
    @PostMapping("/email/verify")
    public ApiResponse<String> verifyEmail(@RequestBody @Validated EmailRequest emailRequest) {
        emailService.sendVerificationEmail(emailRequest.getEmail());
        return ApiResponse.success(SuccessCode.EMAIL_VERIFICATION_SENT);
    }

    // 인증 코드 확인
    @PostMapping("/email/confirm")
    public ApiResponse<String> confirmEmail(@RequestBody @Validated EmailVerificationRequest request) {
        emailService.verifyCode(request.getEmail(), request.getCode());
        return ApiResponse.success(SuccessCode.EMAIL_VERIFIED);
    }

    // 회원가입 (이메일 인증 여부 확인 후 진행)
    @PostMapping
    public ApiResponse<String> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        signUpService.signUp(signUpRequest);
        return ApiResponse.success(SuccessCode.SIGNUP_COMPLETED);
    }
}
