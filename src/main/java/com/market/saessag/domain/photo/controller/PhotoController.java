package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final S3Service s3Service;

    public PhotoController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ApiResponse<List<String>> uploadPhotos(@RequestParam MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileUrl = s3Service.uploadFile(file);
                fileUrls.add(fileUrl);
            }
            return ApiResponse.<List<String>>builder()
                    .status("200")
                    .data(fileUrls)
                    .build();
        } catch (IOException e) {
            return ApiResponse.<List<String>>builder()
                    .status("500")
                    .data(Collections.singletonList("파일 업로드에 실패했습니다." + e.getMessage()))
                    .build();
        }
    }

    @GetMapping()
    public ApiResponse<Map<String, String>> getPresignedUrl(@RequestParam List<String> keys) {
        Map<String, String> urls = s3Service.getPresignedUrl(keys);
        return ApiResponse.<Map<String, String>>builder()
                .status("200")
                .data(urls)
                .build();
    }
}
