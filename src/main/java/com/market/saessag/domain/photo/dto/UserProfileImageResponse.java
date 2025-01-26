package com.market.saessag.domain.photo.dto;

import com.market.saessag.domain.user.entity.User;
import lombok.Getter;

// 프로필 사진 조회
@Getter
public class UserProfileImageResponse {
    private String profileUrl;

    public static UserProfileImageResponse from(User user) {
        UserProfileImageResponse response = new UserProfileImageResponse();
        response.profileUrl = user.getProfileUrl();
        return response;
    }
}