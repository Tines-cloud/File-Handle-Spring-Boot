package com.example.file.handle.controller;

import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandleController {

    @PostMapping(value = Constant.FILE_HANDLE)
    ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                      @RequestParam(value = "serviceType") ServiceType serviceType,
                                      @RequestParam(value = "contentType") ContentType contentType);
}
