package com.example.file.handle.util;

import com.example.file.handle.util.enumerate.ContentType;

public class Constant {
    public static final String FILE_HANDLE = "/upload";
    public static final String FILE_UPLOAD_FAILED_FILE_INVALID = "File is not valid";
    public static final String FILE_UPLOAD_FAILED_SERVICE_TYPE = "Service type is not valid";
    public static final String FILE_UPLOAD_FAILED_CONTENT_TYPE = "Content type is not valid";
    public static final String FILE_UPLOAD_SUCCESS = "Uploaded Successfully";
    public static final String SAMPLE_DOCUMENTS = "sample-documents";
    public static final String SAMPLE_AUDIOS = "sample-audios";
    public static final String SAMPLE_IMAGES = "sample-images";
    public static final String SAMPLE_VIDEOS = "sample-videos";

    public static String decideFolder(ContentType contentType) {
        if (ContentType.DOC.equals(contentType)) {
            return SAMPLE_DOCUMENTS;
        } else if (ContentType.AUDIO.equals(contentType)) {
            return SAMPLE_AUDIOS;
        } else if (ContentType.IMAGE.equals(contentType)) {
            return SAMPLE_IMAGES;
        } else if (ContentType.VIDEO.equals(contentType)) {
            return SAMPLE_VIDEOS;
        } else {
            throw new RuntimeException(Constant.FILE_UPLOAD_FAILED_CONTENT_TYPE);
        }
    }
}
