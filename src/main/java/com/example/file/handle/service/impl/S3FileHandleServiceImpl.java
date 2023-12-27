package com.example.file.handle.service.impl;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.service.S3FileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class S3FileHandleServiceImpl implements S3FileHandleService {
    private static final Logger logger = LoggerFactory.getLogger(S3FileHandleServiceImpl.class);
    @Autowired
    private S3Client amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    public String uploadFile(MultipartFile file, ContentType contentType) {
        logger.info("Upload file method");
        try {
            String path = Constant.decideFolder(contentType) + "/" + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");

            amazonS3.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path).contentType(file.getContentType())
                    .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            logger.info(Constant.FILE_UPLOAD_SUCCESS);
            return Constant.FILE_UPLOAD_SUCCESS;
        } catch (IOException e) {
            logger.error(Constant.FILE_UPLOAD_FAIL + " : " + e.getMessage());
            throw new RuntimeException(Constant.FILE_UPLOAD_FAIL);
        }
    }

    @Override
    public String deleteFile(String fileName, ContentType contentType) {
        logger.info("Delete file method");
        try {
            String fileToDelete = Constant.decideFolder(contentType) + "/" + fileName;

            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileToDelete)
                    .build();

            amazonS3.headObject(headObjectRequest);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileToDelete)
                    .build();

            amazonS3.deleteObject(deleteObjectRequest);
            logger.info(Constant.FILE_DELETE_SUCCESS);
            return Constant.FILE_DELETE_SUCCESS;
        } catch (Exception e) {
            logger.error(Constant.FILE_NOT_FOUND + " : " + e.getMessage());
            throw new RuntimeException(Constant.FILE_NOT_FOUND);
        }
    }

    @Override
    public List<FileInfo> listOfFiles() {
        logger.info("List of files method");
        List<FileInfo> fileInfos = new ArrayList<>();
        try {
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response listObjectsResponse = amazonS3.listObjectsV2(listObjectsRequest);

            for (S3Object s3Object : listObjectsResponse.contents()) {
                if (s3Object.size() == 0) {
                    continue;
                }
                String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Object.key());

                String[] fileName = s3Object.key().split("/");

                FileInfo fileInfo = new FileInfo();

                fileInfo.setFileName(fileName[fileName.length - 1]);
                fileInfo.setContentType(Constant.decideContentType(fileName[0]));
                fileInfo.setUrl(publicUrl);

                fileInfos.add(fileInfo);
            }
        } catch (Exception e) {
            logger.error("List of items failed : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return fileInfos;
    }

    @Override
    public ByteArrayResource downloadFile(String fileName, ContentType contentType) {
        logger.info("Download file method");
        try {
            String fileKey = Constant.decideFolder(contentType) + "/" + fileName;
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            ResponseBytes<GetObjectResponse> responseBytes = amazonS3.getObjectAsBytes(getObjectRequest);

            return new ByteArrayResource(responseBytes.asByteArray());
        } catch (Exception e) {
            logger.error("Error downloading file: " + e.getMessage());
            throw new RuntimeException("Error downloading file: " + e.getMessage(), e);
        }
    }
}
