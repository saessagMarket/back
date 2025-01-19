package com.market.saessag.domain.user.repository;

import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email); // 이메일 중복 검증 방법
    User findByNickname(String nickname);
}