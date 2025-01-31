package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
            return ApiResponse.success(SuccessCode.OK, fileUrls);
        } catch (IOException e) {
            return ApiResponse.error(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @GetMapping()
    public ApiResponse<Map<String, String>> getPresignedUrl(@RequestParam List<String> keys) {
        Map<String, String> urls = s3Service.getPresignedUrl(keys);
        return ApiResponse.success(SuccessCode.OK, urls);
    }
}
