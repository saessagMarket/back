package com.market.saessag.user.repository;

import com.market.saessag.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

//  User findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    /*
        existsByEmail(): 이메일 중복 검증 방법
        특정 email 필드가 존재하면 true, 존재하지 않으면 false 리턴
        -> SignUp 서비스 단에서 검증
     */

}
