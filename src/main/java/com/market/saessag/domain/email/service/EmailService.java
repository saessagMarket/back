package com.market.saessag.domain.email.service;

import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.*;
import com.market.saessag.global.util.EmailVerification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender; // 이메일 발송을 위한 스프링 제공 인터페이스
    private final UserRepository userRepository;
    private final Map<String, EmailVerification> verificationStore = new ConcurrentHashMap<>(); // 이메일 인증 정보를 저장하는 동시성 지원 Map (Key: 이메일, Value: 인증정보)

    /*
        sendVerificationEmail(): 이메일 인증 코드를 생성하고 발송하는 메서드
    */
    public void sendVerificationEmail(String toEmail) {
        try {
            // 이메일 중복 확인
            if (userRepository.existsByEmail(toEmail)) {
                throw new CustomException(ErrorCode.DUPLICATE_EMAIL); // 이미 가입된 이메일인 경우
            }

            // 6자리 랜덤 인증 코드 생성
            String verificationCode = generateVerificationCode();

            // 5분 후 만료되는 인증 정보 생성 및 저장
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
            verificationStore.put(toEmail, new EmailVerification(verificationCode, expirationTime));

            // 이메일 메시지 생성
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("[새싹마켓] 회원가입 인증번호 안내");
            helper.setText(createEmailContent(verificationCode), true); // true는 HTML 형식 사용을 의미

            // 이메일 발송
            mailSender.send(message);

        } catch (MessagingException e) { // 이메일 발송 실패 시 저장된 인증 정보 제거
            verificationStore.remove(toEmail);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED); // 이메일 발송 실패
        }
    }

    /*
        verifyCode(): 인증 코드를 검증하는 메서드
    */
    public void verifyCode(String email, String code) { // (이메일 주소, 사용자가 입력한 인증 코드)
        // 저장된 인증 정보 조회
        EmailVerification verification = verificationStore.get(email);
        if (verification == null) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE); // 잘못된 인증 코드이거나 인증 정보가 없는 경우
        }

        // 만료 여부 확인
        if (verification.isExpired()) {
            verificationStore.remove(email);
            throw new CustomException(ErrorCode.VERIFICATION_EXPIRED); // 인증 코드가 만료된 경우
        }

        // 인증 코드 일치 여부 확인
        if (!verification.getCode().equals(code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE); // 잘못된 인증 코드이거나 인증 정보가 없는 경우
        }

        // 인증 완료 처리
        verification.verify();
    }

    /*
        isEmailVerified(): 이메일 인증 완료 여부를 확인하는 메서드
    */
    public boolean isEmailVerified(String email) {
        EmailVerification verification = verificationStore.get(email);
        if (verification == null) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        return verification.isVerified(); // 인증 완료 여부
    }

    /*
        generateVerificationCode(): 6자리 랜덤 인증 코드를 생성하는 메서드
    */
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    /*
        createEmailContent(): 이메일 본문 HTML을 생성하는 메서드
    */
    private String createEmailContent(String code) {
        return String.format("""
            <div style='text-align: center; margin: 30px;'>
                <h3> saessagMarket </h3>
                <h2>이메일 인증 코드</h2>
                <p> 본 메일은 saessagMarket 회원가입을 위한 이메일 인증입니다.</p>
                <p> 아래의 인증 코드를 입력하여 본인확인을 해주시기 바랍니다.</p>
                <div style='font-size: 24px; font-weight: bold; margin: 20px;'>%s</div>
                <p>이 코드는 5분 동안 유효합니다.</p>
            </div>
            """, code);
    }
    /*
        cleanupExpiredCodes(): 만료된 인증 정보를 정리하는 스케줄링 메서드
    */
    @Scheduled(fixedRate = 300000) // 5분(300000ms)마다 자동 실행
    public void cleanupExpiredCodes() {
        verificationStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

}
