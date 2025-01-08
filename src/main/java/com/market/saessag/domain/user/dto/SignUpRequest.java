// 회원가입
package com.market.saessag.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequest {

  // 이메일 인증은 어떻게 할 것인가?
  private String email;
  private String password;
  private String nickname;

}
