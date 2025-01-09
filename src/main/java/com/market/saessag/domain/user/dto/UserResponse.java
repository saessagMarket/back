package com.market.saessag.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String nickname;
    private String profileUrl;
}