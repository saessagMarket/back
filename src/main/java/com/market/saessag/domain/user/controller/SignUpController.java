package com.market.saessag.domain.user.controller;

import com.market.saessag.domain.email.dto.EmailRequest;
import com.market.saessag.domain.email.dto.EmailVerificationRequest;
import com.market.saessag.domain.email.service.EmailService;
import com.market.saessag.domain.user.service.SignUpService;
import com.market.saessag.domain.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignUpController {

    private final SignUpService signUpService;
    private final EmailService emailService;

    // 이메일 중복 확인 및 인증 코드 발송
    @PostMapping("/sign-up/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody @Validated EmailRequest emailRequest) {
        // 이메일 중복 확인 후 인증 코드 발송
        emailService.sendVerificationEmail(emailRequest.getEmail());
        return ResponseEntity.ok("인증 메일이 발송되었습니다."); // 응답 포맷 통일 필요함
    }

    // 인증 코드 확인
    @PostMapping("/sign-up/email/confirm")
    public ResponseEntity<?> confirmEmail(@RequestBody @Validated EmailVerificationRequest request) {
        emailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok("이메일 인증이 완료되었습니다."); // 응답 포맷 통일 필요함
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        // 이메일 인증 여부 확인 후 회원가입 진행
        signUpService.signUp(signUpRequest);
        return ResponseEntity.ok("회원가입이 완료되었습니다."); // 응답 포맷 통일 필요함
    }

}
