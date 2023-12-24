package com.example.file.handle.service.impl;

import com.example.file.handle.service.FileHandleService;
import com.example.file.handle.service.GCPFileHandleService;
import com.example.file.handle.service.S3FileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileHandleServiceImpl implements FileHandleService {
    @Autowired
    private GCPFileHandleService gcpFileHandleService;
    @Autowired
    private S3FileHandleService s3FileHandleService;

    @Override
    public String uploadFile(MultipartFile file, ServiceType serviceType, ContentType contentType) {

        if (file.isEmpty()){
            throw new RuntimeException(Constant.FILE_UPLOAD_FAILED_FILE_INVALID);
        }

        if (serviceType.equals(ServiceType.GCP)) {
            return gcpFileHandleService.uploadFile(file,contentType);
        } else if (serviceType.equals(ServiceType.S3)) {
            return s3FileHandleService.uploadFile(file,contentType);
        } else {
            return Constant.FILE_UPLOAD_FAILED_SERVICE_TYPE;
        }
    }
}
