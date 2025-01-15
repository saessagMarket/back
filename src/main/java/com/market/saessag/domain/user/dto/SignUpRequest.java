// 회원가입
package com.market.saessag.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

  private String email; // 가입할 이메일
  private String password;
  private String nickname;

}
