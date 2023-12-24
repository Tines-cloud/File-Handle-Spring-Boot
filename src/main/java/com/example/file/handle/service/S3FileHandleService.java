package com.example.file.handle.service;

import com.example.file.handle.util.enumerate.ContentType;
import org.springframework.web.multipart.MultipartFile;

public interface S3FileHandleService {
    String uploadFile(MultipartFile file, ContentType contentType);
}
