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

  @PostMapping("/signIn")
  public SignInResponse signIn(@RequestBody SignInRequest req) {

    User user = userRepository.findByEmailAndPassword(req.getEmail(), req.getPassword());
    SignInResponse signInResponse = new SignInResponse();
    signInResponse.setEmail(user.getEmail());
    signInResponse.setProfileUrl(user.getProfileUrl());
    signInResponse.setNickname(user.getNickname());

    return signInResponse;
  }
}
