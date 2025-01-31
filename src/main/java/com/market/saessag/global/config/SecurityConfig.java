package com.market.saessag.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/products", "/api/products/*").permitAll()  // 상품 조회 관련 엔드포인트
                        .requestMatchers("/api/sign-up", "/api/sign-in").permitAll()     // 인증 관련 엔드포인트
                        .requestMatchers("/error").permitAll()                             // 에러 관련 엔드포인트
                        .anyRequest().authenticated()                                     // 나머지는 인증 필요
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)    // 인증 실패 처리
                );
        
        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 개발 환경과 배포 환경을 하나의 List로 설정
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",                  // 로컬 개발 서버
                "https://saessagmarket.netlify.app"       // Netlify 배포 환경
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config); // 모든 /api 경로에 대해 적용
        return source;
    }

}

