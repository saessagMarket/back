package com.market.saessag.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // 이메일 중복 가입 방지
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileUrl;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String role; // 사용자 권한

    @Column(name = "email_verified")
    private boolean emailVerified = false; // 이메일 인증 여부

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 시간

    @Builder
    public User(String email, String password, String profileUrl, String nickname, String role) {
        this.email = email;
        this.password = password;
        this.profileUrl = profileUrl;
        this.role = role;
        this.emailVerified = true; // 회원가입 시점에는 이미 인증이 완료된 상태
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now().withNano(0);
        this.updatedAt = LocalDateTime.now().withNano(0);
    }

}