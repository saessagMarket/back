package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileImageController {
    private final S3Service s3Service;

    /*
         프로필 사진 업로드
         사용자 관점에서는 프로필 사진 업로드와 수정이 동일한 방식으로 진행됨.
         따라서 하나의 Patch 메서드에서 동작함.
     */
    @PatchMapping("/upload-image")
    public ApiResponse<String> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            if (email == null) {
                return ApiResponse.error(ErrorCode.UNAUTHORIZED);
            }

            String fileUrl = s3Service.uploadProfileImage(file, email);
            return ApiResponse.success(SuccessCode.UPLOAD_SUCCESS, fileUrl);
        } catch (IOException e) {
            return ApiResponse.error(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    // 프로필 사진 조회
    @GetMapping
    public ApiResponse<Map<String, String>> getProfileImageUrl(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ApiResponse.<Map<String, String>>builder()
                    .status("401")
                    .message("로그인이 필요합니다.")
                    .build();
        }

        Map<String, String> urls = s3Service.getProfileImageUrl(email);
        return ApiResponse.<Map<String, String>>builder()
                .status("200")
                .data(urls)
                .build();
    }

}