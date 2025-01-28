package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.photo.service.S3Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
            }

            String fileUrl = s3Service.uploadProfileImage(file, email);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 프로필 사진 조회
    @GetMapping
    public ResponseEntity<?> getProfileImageUrl(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        return ResponseEntity.ok(s3Service.getProfileImageUrl(email));
    }
}