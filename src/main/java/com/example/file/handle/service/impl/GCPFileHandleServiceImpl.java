package com.example.file.handle.service.impl;

import com.example.file.handle.service.GCPFileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GCPFileHandleServiceImpl implements GCPFileHandleService {
    @Autowired
    private Storage storage;
    @Value("${gcp.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, ContentType contentType) {
        try {
            String fileName = Constant.decideFolder(contentType) + "/" + file.getOriginalFilename();

            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

            storage.create(blobInfo, file.getBytes());
            return Constant.FILE_UPLOAD_SUCCESS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String fileName, ContentType contentType) {
        try {
            String fileToDelete = Constant.decideFolder(contentType) + "/" + fileName;
            Blob blob = storage.get(bucketName, fileToDelete);
            blob.delete();
            return Constant.FILE_DELETE_SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException(Constant.FILE_DELETE_FAILED_FILE_NOT_FOUND);
        }
    }
}
