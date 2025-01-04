package com.market.saessag.user.controller;

import com.market.saessag.user.dto.SignUpRequest;
import com.market.saessag.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;
    // 현재는 필드 주입 방식이지만 나중에 생성자 주입 방식으로 바꾸면 좋을듯

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest){
        signUpService.signUpProcess(signUpRequest);

        return ResponseEntity.ok().build();
    }

}
