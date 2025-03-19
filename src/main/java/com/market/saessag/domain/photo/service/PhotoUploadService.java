package com.market.saessag.domain.photo.service;

import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
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
        try {
            for (MultipartFile file : files) {
                String fileUrl = s3Service.uploadFile(file); // S3 파일 업로드 호출
                fileUrls.add(fileUrl);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return fileUrls;
    }
}
