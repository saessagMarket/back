package com.market.saessag.user;

import lombok.Data;

@Data
public class SignInRequest {

  private String email;
  private String password;
}
