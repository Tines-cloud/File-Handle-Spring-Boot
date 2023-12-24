package com.example.file.handle.service;

import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandleService {
    String uploadFile(MultipartFile file, ServiceType serviceType, ContentType contentType);
}
