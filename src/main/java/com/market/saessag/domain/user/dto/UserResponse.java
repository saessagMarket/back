package com.market.saessag.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String nickname;
    private String profileUrl;
}