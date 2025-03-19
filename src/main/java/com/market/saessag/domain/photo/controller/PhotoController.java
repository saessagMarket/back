package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.auth.AuthService;
import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {
    private final AuthService authService;
    private final S3Service s3Service;

    @PostMapping("/upload")
    public ApiResponse<List<String>> uploadPhotos(@RequestParam MultipartFile[] files, HttpServletRequest request) {
        try {
            authService.getAuthenticatedEmail(request); // 로그인 한 사용자만 사진 업로드 가능

            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileUrl = s3Service.uploadFile(file);
                fileUrls.add(fileUrl);
            }
            return ApiResponse.success(fileUrls);
        } catch (HttpSessionRequiredException e) {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED);
        }
    }

    @GetMapping()
    public ApiResponse<Map<String, String>> getPresignedUrl(@RequestParam List<String> keys) {
        return ApiResponse.success(s3Service.getPresignedUrl(keys));
    }
}
