package com.market.saessag.domain.user.service;

import com.market.saessag.domain.user.dto.SignInRequest;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        // 사용자 조회
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return SignInResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .nickname(user.getNickname())
                .build();
    }
}
