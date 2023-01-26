package com.modugarden.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    public String uploadFile(MultipartFile multipartFile, Long userId, String imageType) {
        if (multipartFile.isEmpty()) {
            return getDefaultRandomProfileImageUrl();
        }
        String savedFileName = getSavedFileName(multipartFile, userId, imageType);
        ObjectMetadata metadata = new ObjectMetadata();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(bucketName, savedFileName, inputStream, metadata);
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new BusinessException(ErrorMessage.INVALID_FILE_UPLOAD);
        }
        return getResourceUrl(savedFileName);
    }

    public String getDefaultRandomProfileImageUrl() {
        return String.format("%s/%s/%s.png", defaultUrl, "profileImage", (int)(Math.random() * 3 + 1));
    }

    public void deleteFile(String fileUrl) {
        String fileName = getFileNameFromResourceUrl(fileUrl);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private String getSavedFileName(MultipartFile multipartFile, Long userId, String imageType) {
        return String.format("user%s/%s/%s-%s",
                userId, imageType, getRandomUUID(), multipartFile.getOriginalFilename());
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getResourceUrl(String savedFileName) {
        return amazonS3Client.getResourceUrl(bucketName, savedFileName);
    }

    private String getFileNameFromResourceUrl(String fileUrl) {
        return fileUrl.replace(defaultUrl + "/", "");
    }
}