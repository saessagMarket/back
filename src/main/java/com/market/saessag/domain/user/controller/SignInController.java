package com.market.saessag.domain.user.controller;

import com.market.saessag.domain.user.dto.SignInRequest;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.service.SignInService;
import com.market.saessag.global.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(
            @Validated @RequestBody SignInRequest signInRequest, HttpSession session){

        SignInResponse signInResponse = signInService.signIn(signInRequest);
        session.setAttribute("user", signInResponse); // 세션에 로그인 정보 저장

        ApiResponse<SignInResponse> response = ApiResponse.<SignInResponse>builder()
                .status("200")
                .data(signInResponse)
                .build();

        return ResponseEntity.ok(response);
    }

}
