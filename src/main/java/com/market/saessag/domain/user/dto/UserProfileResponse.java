package com.market.saessag.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private final Long id;
    private String nickname;
    private String profileUrl;
}