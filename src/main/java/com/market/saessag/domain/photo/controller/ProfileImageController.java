package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.auth.AuthService;
import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import com.market.saessag.global.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileImageController {
    private final AuthService authService;
    private final S3Service s3Service;

    // 프로필 사진 업로드
    @PatchMapping("/upload-image") // 사용자 관점에서는 프로필 사진 업로드와 수정이 동일한 방식으로 진행됨. 따라서 하나의 Patch 메서드에서 동작함.
    public ApiResponse<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        String email = SessionUtils.getUserEmail();
        String fileUrl = s3Service.uploadProfileImage(file, email);
        return ApiResponse.success(SuccessCode.UPLOAD_SUCCESS, fileUrl);
    }

    // 프로필 사진 조회(현재 본인 프로필 사진만 확인 가능)
    @GetMapping
    public ApiResponse<Map<String, String>> getProfileImageUrl() {

        try {
            String email = authService.getAuthenticatedEmail();
            return ApiResponse.success(s3Service.getProfileImageUrl(email));
        } catch (CustomException e) {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED);
        }
    }
}
