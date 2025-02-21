package com.market.saessag.domain.user.service;

import com.market.saessag.domain.email.service.EmailService;
import com.market.saessag.domain.user.dto.SignUpRequest;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    @Transactional
    public ApiResponse<String> signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();

        // 1. 이메일 인증 여부 먼저 확인
        if (!emailService.isEmailVerified(email)) {
            return ApiResponse.error(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 2. 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            return ApiResponse.error(ErrorCode.DUPLICATE_EMAIL);
        }

        // 3. 회원가입 진행
        try {
            User user = User.builder()
                    .email(email)
                    .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                    .nickname(signUpRequest.getNickname())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            return ApiResponse.success(SuccessCode.SIGNUP_SUCCESS);
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.SIGNUP_FAILED);
        }
    }
}
