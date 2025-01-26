package com.market.saessag.domain.photo.controller;

import com.market.saessag.domain.photo.dto.UserProfileImageResponse;
import com.market.saessag.domain.photo.service.S3Service;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileImageController {
    private final S3Service s3Service;
    private final UserRepository userRepository;

    // 프로필 사진 업로드
    @PatchMapping("/image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            // 세션에서 이메일 확인
            String email = (String) session.getAttribute("email");
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
            }

            // 사용자 조회
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

            // 새 이미지 업로드
            String fileUrl = s3Service.uploadFile(file);
            user.setProfileUrl(fileUrl);
            userRepository.save(user);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 프로필 사진 조회
    @GetMapping
    public ResponseEntity<?> getProfile(HttpSession session) {
        // 세션에서 이메일 가져오기
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        // DB에서 사용자 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 프로필 사진 URL 반환
        return ResponseEntity.ok(UserProfileImageResponse.from(user));
    }
}