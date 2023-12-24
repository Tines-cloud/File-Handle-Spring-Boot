package com.example.file.handle.service.impl;

import com.example.file.handle.modal.FileInfo;
import com.example.file.handle.service.GCPFileHandleService;
import com.example.file.handle.util.Constant;
import com.example.file.handle.util.enumerate.ContentType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GCPFileHandleServiceImpl implements GCPFileHandleService {
    private static final Logger logger = LoggerFactory.getLogger(GCPFileHandleServiceImpl.class);

    @Autowired
    private Storage storage;
    @Value("${gcp.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, ContentType contentType) {
        logger.info("Upload file method");
        try {
            String fileName = Constant.decideFolder(contentType) + "/" + file.getOriginalFilename();

            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    //  .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                    .setContentType(file.getContentType())
                    .build();

            storage.create(blobInfo, file.getBytes());
            logger.info(Constant.FILE_UPLOAD_SUCCESS);
            return Constant.FILE_UPLOAD_SUCCESS;
        } catch (IOException e) {
            logger.info(Constant.FILE_UPLOAD_FAIL + " : " + e.getMessage());
            throw new RuntimeException(Constant.FILE_HANDLE_FAILED_SERVICE_TYPE);
        }
    }

    @Override
    public String deleteFile(String fileName, ContentType contentType) {
        logger.info("Delete file method");
        try {
            String fileToDelete = Constant.decideFolder(contentType) + "/" + fileName;
            Blob blob = storage.get(bucketName, fileToDelete);
            blob.delete();
            logger.info(Constant.FILE_DELETE_SUCCESS);
            return Constant.FILE_DELETE_SUCCESS;
        } catch (Exception e) {
            logger.info(Constant.FILE_NOT_FOUND + " : " + e.getMessage());
            throw new RuntimeException(Constant.FILE_NOT_FOUND);
        }
    }

    @Override
    public List<FileInfo> listOfFiles() {
        logger.info("List of files method");
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

    @Override
    public ByteArrayResource downloadFile(String fileName, ContentType contentType) {
        logger.info("Download file method");
        try {
            String fileToDownload = Constant.decideFolder(contentType) + "/" + fileName;
            Blob blob = storage.get(bucketName, fileToDownload);
            return new ByteArrayResource(blob.getContent());
        } catch (Exception e) {
            logger.error("Error downloading file: " + e.getMessage());
            throw new RuntimeException(Constant.FILE_NOT_FOUND);
        }
    }
}
