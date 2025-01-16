package com.market.saessag.domain.photo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @PostConstruct
    public void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");

        if (accessKey == null || secretKey == null) {
            throw new IllegalArgumentException("액세스 키, 또는 시크릿 키 환경 변수가 설정되지 않았습니다.");
        }

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey,secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException{
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    tempFile.toPath()
            );

            tempFile.delete();

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        } catch (S3Exception e) {
            throw new RuntimeException("S3에 파일 업로드 중 문제가 발생했습니다. : " + e.getMessage(), e);
        }
    }

    public Map<String, String> getPresignedUrl(List<String> keys) {
        return keys.stream()
                .collect(Collectors.toMap(
                        fileName -> fileName,
                        fileName -> {
                            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(fileName)
                                    .build();

                            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                    .getObjectRequest(getObjectRequest)
                                    .signatureDuration(Duration.ofMinutes(30))
                                    .build();

                            return s3Presigner.presignGetObject(presignRequest).url().toString();
                        }
                ));
    }
}
