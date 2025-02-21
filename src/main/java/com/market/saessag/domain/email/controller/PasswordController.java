package com.market.saessag.domain.email.controller;

import com.market.saessag.domain.email.dto.EmailRequest;
import com.market.saessag.domain.email.dto.PasswordChangeRequest;
import com.market.saessag.domain.email.service.PasswordService;
import com.market.saessag.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordController {
    private final PasswordService passwordService;

    // 비밀번호 찾기
    @PostMapping("/find")
    public ApiResponse<String> findPassword(@RequestBody @Validated EmailRequest request) {
        return passwordService.sendTemporaryPassword(request.getEmail()); // 이메일로 임시 비밀번호 발급
    }

    // 비밀번호 변경
    @PatchMapping("/change")
    public ApiResponse<String> changePassword(@RequestBody @Validated PasswordChangeRequest request) {
        return passwordService.changePassword(request);
    }
}
