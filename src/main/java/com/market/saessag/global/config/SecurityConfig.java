package com.market.saessag.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        // 단방향 해시 암호화

        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        // 경로 지정 로직 작성
        httpSecurity
                .authorizeHttpRequests((auth) -> auth // 인가
                        .requestMatchers("/api/**").permitAll() // API 엔드포인트
                        .requestMatchers("/sign-up", "/sign-in").permitAll() // 회원가입, 로그인
                        .requestMatchers("/admin").hasRole("ADMIN") // 관리자
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // 마이페이지
                        .anyRequest().authenticated() // 이외의 경로
                );

        httpSecurity
                .csrf((auth) -> auth.disable()); // 개발 환경에서만 disable

        return httpSecurity.build();
    }
}

