package com.market.saessag.domain.user.service;

import com.market.saessag.domain.user.dto.SignInRequest;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
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
        User user = userRepository.findByEmail(signInRequest.getEmail());

        validateUserExists(signInRequest.getEmail()); // 이메일 검증
        validatePassword(signInRequest.getPassword(), user.getPassword()); // 비밀번호 검증

        return SignInResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .nickname(user.getNickname())
                .build();
    }

    private void validateUserExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

}
