package com.example.file.handle.service.impl;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.service.GCPFileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.example.file.handle.util.enumerate.ServiceType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GCPFileHandleServiceImpl implements GCPFileHandleService {
    @Autowired
    private Storage storage;
    @Value("${gcp.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, ContentType contentType) {
        try {
            String fileName = Constant.decideFolder(contentType) + "/" + file.getOriginalFilename();

            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    //  .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                    .setContentType(file.getContentType())
                    .build();

            storage.create(blobInfo, file.getBytes());
            return Constant.FILE_UPLOAD_SUCCESS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String fileName, ContentType contentType) {
        try {
            String fileToDelete = Constant.decideFolder(contentType) + "/" + fileName;
            Blob blob = storage.get(bucketName, fileToDelete);
            blob.delete();
            return Constant.FILE_DELETE_SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException(Constant.FILE_DELETE_FAILED_FILE_NOT_FOUND);
        }
    }

    @Override
    public List<FileInfo> listOfFiles() {
        List<FileInfo> fileInfos = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName);
        for (Blob blob : blobs.iterateAll()) {
            String publicLink = String.format("https://storage.googleapis.com/%s/%s", bucketName, blob.getName());

            String[] fileName = blob.getName().split("/");

            FileInfo fileInfo = new FileInfo();

            fileInfo.setFileName(fileName[fileName.length - 1]);
            fileInfo.setContentType(Constant.decideContentType(fileName[0]));
            fileInfo.setUrl(publicLink);

            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }
}
