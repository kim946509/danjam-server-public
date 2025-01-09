package com.example.danjamserver.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public MinioService(MinioClient minioClient, ObjectMapper objectMapper) {
        this.minioClient = minioClient;
        this.objectMapper = objectMapper;
    }

    public void createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, MinioException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public String uploadFileAndGetUrl(String bucketName, String fileName, MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, MinioException {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(fileName)
                        .expiry(7, TimeUnit.DAYS)
                        .build());
    }

    /**
     * 현재 시간을 한국 시간에 맞추고 지정된 형식으로 파일명을 생성합니다.
     *
     * @param username 사용자 이름
     * @param suffix 파일명 접미사
     * @param extension 파일 확장자
     * @return 생성된 파일명
     */
    public String generateFileName(String username, String suffix, String extension) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return username + "_" + now.format(formatter) + "_" + suffix + "." + extension;
    }

    /**
     * MultipartFile 객체에서 파일 확장자를 추출합니다.
     *
     * @param file 업로드할 파일
     * @return 파일 확장자
     */
    public String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }
        return "";
    }

    /**
     * 이미지 파일을 업로드합니다.
     *
     * @param file 업로드할 파일
     * @param bucketName 파일이 저장될 버킷명
     * @param fileName 저장할 파일명
     * @return MinIO 에서 제공된 파일 접근 URL
     * @throws Exception 파일 업로드 오류 시 예외 발생
     */
    public String uploadFile(MultipartFile file, String bucketName, String fileName) throws Exception {
        if (file != null && !file.isEmpty()) {
            createBucket(bucketName);
            return uploadFileAndGetUrl(bucketName, fileName, file);
        }
        throw new Exception("File upload failed and no URL was obtained.");
    }

    public JsonNode getFile(String bucketName, String fileName) throws Exception {
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())) {
            return objectMapper.readTree(inputStream);
        }
    }

}


