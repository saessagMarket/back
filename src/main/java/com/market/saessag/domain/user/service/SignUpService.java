package com.market.saessag.user.service;

import com.market.saessag.user.dto.SignUpRequest;
import com.market.saessag.user.entity.User;
import com.market.saessag.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private UserRepository userRepository;
    // 현재는 필드 주입 방식이지만 나중에 생성자 주입 방식으로 바꾸면 좋을듯

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUpProcess(SignUpRequest signUpRequest){

        // DB에 이미 동일한 email을 가진 회원이 존재하는지 검증하는 로직
        boolean isUser = userRepository.existsByEmail(signUpRequest.getEmail());
        if(isUser){
            return; // 이미 회원이 존재하면 강제 return
        }

        // 회원이 존재하지 않으면 아래 로직 수행
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword())); // 비밀번호 암호화
        user.setNickname(signUpRequest.getNickname());
        user.setRole("ROLE_ADMIN");
        
        userRepository.save(user);
    }

}
