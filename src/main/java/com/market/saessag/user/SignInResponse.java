package com.market.saessag.user.dto;

import lombok.Data;

@Data
public class SignInResponse {

  private String email;
  private String profileUrl;
  private String nickname;
}
