package com.codewithdang.kltn_giaphaonline.service.minio_media;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadVideo(MultipartFile file, String folder);

    String uploadImage(MultipartFile file, String folder);

    String uploadFile(MultipartFile file, String folder);

    String getPresignedUrl(String objectName);

    void deleteFile(String objectName);
}
