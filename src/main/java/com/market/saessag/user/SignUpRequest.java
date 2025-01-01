package com.market.saessag.user;

import lombok.Data;

@Data
public class SignUpRequest {

  private String email;
  private String password;
  private String profileUrl;
  private String nickname;
}
