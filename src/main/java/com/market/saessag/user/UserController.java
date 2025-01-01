package com.market.saessag.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  @PostMapping("/signUp")
  public boolean signUp(@RequestBody SignUpRequest req) {

    User user = new User();
    user.setEmail(req.getEmail());
    user.setPassword(req.getPassword());
    user.setProfileUrl(req.getProfileUrl());
    user.setNickname(req.getNickname());

    userRepository.save(user);

    return true;
  }


}
