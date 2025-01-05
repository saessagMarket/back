package com.market.saessag.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true) // 이메일 중복 가입 방지
  private String email;

  @Column(nullable = false)
  private String password;

  private String profileUrl;

  @Column(nullable = false)
  private String nickname;

  private String role; // 권한

  @Builder
  public User(String email, String password, String profileUrl, String nickname) {
    this.email = email;
    this.password = password;
    this.profileUrl = profileUrl;
    this.nickname = nickname;
  }

}
