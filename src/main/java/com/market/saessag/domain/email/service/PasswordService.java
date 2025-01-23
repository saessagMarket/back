package com.market.saessag.domain.email.service;

import com.market.saessag.domain.email.dto.PasswordChangeRequest;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.util.TemporaryPassword;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender; // 이메일 발송을 위한 스프링 제공 인터페이스
    private final BCryptPasswordEncoder passwordEncoder;
    private final Map<String, TemporaryPassword> temporaryPasswordStore = new ConcurrentHashMap<>(); // 임시 저장소 추가

    
    // 임시 비밀번호 발급
    public void sendTemporaryPassword(String email) {
        try {
            // 1. 사용자 존재 여부 확인
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 2. 임시 비밀번호 생성
            String temporaryPassword = generateTemporaryPassword();

            // 3. 임시 비밀번호 저장 (5분 유효)
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
            TemporaryPassword tempPassword = new TemporaryPassword(temporaryPassword, expirationTime);
            temporaryPasswordStore.put(email, tempPassword);

            // 4. 이메일 발송
            sendPasswordEmail(email, temporaryPassword);

            // 5. 이메일 발송 성공 시 DB 업데이트
            user.updatePassword(passwordEncoder.encode(temporaryPassword));
            userRepository.save(user);

        } catch (CustomException e) {
            temporaryPasswordStore.remove(email);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    // 임시 비밀번호 생성 (10자리)
    private String generateTemporaryPassword() {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        // 대문자, 소문자, 숫자 포함
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    private void sendPasswordEmail(String email, String temporaryPassword) {
        try {
            // 이메일 메시지 생성
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("[새싹마켓] 임시 비밀번호 발급");
            helper.setText(createEmailContent(temporaryPassword), true);

            // 이메일 발송
            mailSender.send(message);
        } catch (MessagingException e) {// 이메일 발송 실패 시 임시 저장소에서 제거
            temporaryPasswordStore.remove(email);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED); // 이메일 발송 실패
        }
    }

    private String createEmailContent(String temporaryPassword) {
        return String.format("""
            <div style='text-align: center; margin: 30px;'>
                <h3> saessagMarket </h3>
                <h2>임시 비밀번호 발급</h2>
                <p> 본 메일은 saessagMarket 임시 비밀번호 발급을 위한 이메일입니다.</p>
                <p>아래의 임시 비밀번호로 로그인해 주세요.</p>
                <p>보안을 위해 로그인 후 비밀번호를 변경해 주세요.</p>
                <p style='font-size: 24px; font-weight: bold; margin: 20px;'>%s</p>
            </div>
            """, temporaryPassword);
    }

    // 5분마다 만료된 임시 비밀번호 정리
    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredPasswords() {
        temporaryPasswordStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    // 비밀번호 변경
    @Transactional
    public void changePassword(PasswordChangeRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호 암호화 및 업데이트
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
