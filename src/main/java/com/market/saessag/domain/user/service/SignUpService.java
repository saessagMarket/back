// 회원가입 처리
package com.market.saessag.domain.user.service;

import com.market.saessag.domain.email.service.EmailService;
import com.market.saessag.domain.user.dto.SignUpRequest;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
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

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();

        // 1. 이메일 인증 여부 먼저 확인
        if (!emailService.isEmailVerified(email)) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED); // 이메일 미인증 시
        }

        // 2. 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL); // 중복 이메일
        }

        // 3. 회원가입 진행
        User user = User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

}