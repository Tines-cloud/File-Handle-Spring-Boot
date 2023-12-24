package com.example.file.handle.service;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileHandleService {
    String uploadFile(MultipartFile file, ServiceType serviceType, ContentType contentType);

    String deleteFile(String fileName, ServiceType serviceType, ContentType contentType);

    List<FileInfo> listOfFiles(ServiceType serviceType);

    ByteArrayResource downloadFile(String fileName, ServiceType serviceType, ContentType contentType);
}
