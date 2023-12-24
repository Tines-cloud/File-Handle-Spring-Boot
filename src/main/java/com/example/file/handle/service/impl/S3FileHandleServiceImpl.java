package com.example.file.handle.service.impl;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.service.S3FileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3FileHandleServiceImpl implements S3FileHandleService {
    @Autowired
    private S3Client amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

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

    @Override
    public String deleteFile(String fileName, ContentType contentType) {
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
            return Constant.FILE_DELETE_SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException(Constant.FILE_DELETE_FAILED_FILE_NOT_FOUND);
        }
    }

    @Override
    public List<FileInfo> listOfFiles() {
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
            // Handle exceptions as needed
            e.printStackTrace();
        }

        return fileInfos;
    }
}
