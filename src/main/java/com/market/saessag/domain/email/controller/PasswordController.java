package com.market.saessag.domain.email.controller;

import com.market.saessag.domain.email.dto.EmailRequest;
import com.market.saessag.domain.email.dto.PasswordChangeRequest;
import com.market.saessag.domain.email.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/find")
    public ResponseEntity<?> findPassword(@RequestBody @Validated EmailRequest request) {
        // 이메일로 임시 비밀번호 발급
        passwordService.sendTemporaryPassword(request.getEmail());
        return ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다."); // 응답 포맷 통일 필요함
    }

    @PatchMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody @Validated PasswordChangeRequest request) {
        passwordService.changePassword(request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다."); // 응답 포맷 통일 필요함
    }
}
