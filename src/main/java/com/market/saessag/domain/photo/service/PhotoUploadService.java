package com.market.saessag.domain.photo.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// 파일 업로드 로직을 관리하는 서비스
@Service
public class PhotoUploadService {

    private final S3Service s3Service;

    public PhotoUploadService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public List<String> uploadPhotos(MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileUrl = s3Service.uploadFile(file);
            fileUrls.add(fileUrl);
        }
        return fileUrls;
    }
}
