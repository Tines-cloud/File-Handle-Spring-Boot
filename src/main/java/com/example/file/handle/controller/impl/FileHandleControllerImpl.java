package com.example.file.handle.controller.impl;

import com.example.file.handle.controller.FileHandleController;
import com.example.file.handle.service.FileHandleService;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class FileHandleControllerImpl implements FileHandleController {
    @Autowired
   private FileHandleService fileHandleService;

   @Override
   public ResponseEntity<String> uploadFile(MultipartFile file, ServiceType serviceType, ContentType contentType) {
       try {
           String response = fileHandleService.uploadFile(file, serviceType,contentType);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           String errorMessage = "Failed to upload file. " + e.getMessage();
           return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }


}
