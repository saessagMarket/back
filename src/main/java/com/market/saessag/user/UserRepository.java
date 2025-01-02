package com.market.saessag.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmailAndPassword(String email, String password);

}
