package com.market.saessag.user;

import lombok.Data;

@Data
public class SignInResponse {

  private String email;
  private String profileUrl;
  private String nickname;
}
