package com.example.file.handle.service.impl;

import com.example.file.handle.service.S3FileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3FileHandleServiceImpl implements S3FileHandleService {
    @Autowired
    private S3Client amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, ContentType contentType) {
        try {
            String path = Constant.decideFolder(contentType) + "/" + file.getOriginalFilename();

            amazonS3.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path).contentType(file.getContentType())
                    .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return Constant.FILE_UPLOAD_SUCCESS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
