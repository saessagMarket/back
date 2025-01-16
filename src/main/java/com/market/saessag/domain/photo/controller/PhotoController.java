package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.photo.service.S3Service;
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
    public ResponseEntity<List<String>> uploadPhotos(@RequestParam MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileUrl = s3Service.uploadFile(file);
                fileUrls.add(fileUrl);
            }
            return ResponseEntity.ok(fileUrls); //ApiResponse 사용하기
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Collections.singletonList("파일 업로드에 실패했습니다." + e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam List<String> keys) {
        Map<String, String> urls = s3Service.getPresignedUrl(keys);
        return ResponseEntity.ok(urls);
    }
}
