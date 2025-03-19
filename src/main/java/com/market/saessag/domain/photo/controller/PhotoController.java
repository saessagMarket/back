package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.auth.AuthService;
import com.market.saessag.domain.photo.service.PhotoUploadService;
import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {
    private final AuthService authService;
    private final PhotoUploadService photoUploadService;
    private final S3Service s3Service;

    @PostMapping("/upload")
    public ApiResponse<List<String>> uploadPhotos(@RequestParam MultipartFile[] files) {
        authService.getAuthenticatedEmail(); // 로그인 한 사용자만 사진 업로드 가능
        List<String> fileUrls = photoUploadService.uploadPhotos(files); // 예외는 서비스에서 처리
        return ApiResponse.success(fileUrls);
    }

    @GetMapping()
    public ApiResponse<Map<String, String>> getPresignedUrl(@RequestParam List<String> keys) {
        return ApiResponse.success(s3Service.getPresignedUrl(keys));
    }
}
