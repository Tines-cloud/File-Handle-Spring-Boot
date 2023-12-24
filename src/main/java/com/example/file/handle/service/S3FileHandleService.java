package com.example.file.handle.service;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.util.enumerate.ContentType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3FileHandleService {
    String uploadFile(MultipartFile file, ContentType contentType);

    String deleteFile(String fileName, ContentType contentType);

    List<FileInfo> listOfFiles();

    ByteArrayResource downloadFile(String fileName, ContentType contentType);
}
