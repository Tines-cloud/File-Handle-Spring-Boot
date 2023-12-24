package com.example.file.handle.controller;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileHandleController {
    @GetMapping(value = Constant.FILE_LOAD)
    ResponseEntity<List<FileInfo>> listOfFiles(@RequestParam(value = "serviceType") ServiceType serviceType);

    @GetMapping(value = Constant.FILE_DOWNLOAD)
    ResponseEntity<Resource> downloadFile(
            @PathVariable(value = "fileName") String fileName,
            @RequestParam(value = "serviceType") ServiceType serviceType,
            @RequestParam(value = "contentType") ContentType contentType);

    @DeleteMapping(value = Constant.FILE_REMOVE)
    ResponseEntity<String> deleteFile(
            @PathVariable(value = "fileName") String fileName,
            @RequestParam(value = "serviceType") ServiceType serviceType,
            @RequestParam(value = "contentType") ContentType contentType);

    @PostMapping(value = Constant.FILE_UPLOAD)
    ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                      @RequestParam(value = "serviceType") ServiceType serviceType,
                                      @RequestParam(value = "contentType") ContentType contentType);
}
