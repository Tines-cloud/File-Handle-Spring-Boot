package com.example.file.handle.controller.impl;

import com.example.file.handle.controller.FileHandleController;
import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.service.FileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(Constant.FILE_HANDLE)
public class FileHandleControllerImpl implements FileHandleController {
    @Autowired
    private FileHandleService fileHandleService;

    @Override
    public ResponseEntity<List<FileInfo>> listOfFiles(ServiceType serviceType) {
        try {
            List<FileInfo> files = fileHandleService.listOfFiles(serviceType);

            return ResponseEntity.ok(files);
        } catch (Exception e) {
            String errorMessage = "Failed to load files. " + e.getMessage();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String fileName, ServiceType serviceType, ContentType contentType) {
        ByteArrayResource resource = fileHandleService.downloadFile(fileName, serviceType, contentType);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_OCTET_STREAM).
                headers(headers).body(resource);
    }

    @Override
    public ResponseEntity<String> deleteFile(String fileName, ServiceType serviceType, ContentType contentType) {
        try {
            String response = fileHandleService.deleteFile(fileName, serviceType, contentType);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to delete file. " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, ServiceType serviceType, ContentType contentType) {
        try {
            String response = fileHandleService.uploadFile(file, serviceType, contentType);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to upload file. " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
