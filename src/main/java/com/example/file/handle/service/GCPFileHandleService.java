package com.example.file.handle.service;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.util.enumerate.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GCPFileHandleService {
    String uploadFile(MultipartFile file, ContentType contentType);

    String deleteFile(String fileName, ContentType contentType);

    List<FileInfo> listOfFiles();
}
