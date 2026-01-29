package com.codewithdang.kltn_giaphaonline.service.minio;

import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import io.minio.*;
import io.minio.http.Method;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioServiceImpl implements MinioService {

    MinioClient minioClient;

    @NonFinal
    @Value("${minio.bucket}")
    String bucketName;

    @NonFinal
    @Value("${minio.presign-expire}")
    int expire;

    /**
     * Upload Ảnh (max 5MB)
     */
    @Override
    public String uploadImage(MultipartFile file, String folder) {
        validateFile(file, 5 * 1024 * 1024, List.of("image/jpeg", "image/png", "image/jpg"), ErrorCode.INVALID_FILE_TYPE);
        return executeUpload(file, folder);
    }

    /**
     * Upload Video (max 100MB)
     */
    @Override
    public String uploadVideo(MultipartFile file, String folder) {
        validateFile(file, 100 * 1024 * 1024, List.of("video/mp4", "video/x-matroska", "video/x-msvideo"), ErrorCode.INVALID_VIDEO_TYPE);
        return executeUpload(file, folder);
    }

    /**
     * Hàm upload gốc (Old uploadFile)
     */
    @Override
    public String uploadFile(MultipartFile file, String folder) {
        return uploadImage(file, folder);
    }

    @Override
    public String getPresignedUrl(String objectName) {
        if (objectName == null || objectName.isBlank()) return null;
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expire)
                            .build()
            );
        } catch (Exception e) {
            log.error("Lỗi tạo Presigned URL cho {}: {}", objectName, e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteFile(String objectName) {
        if (objectName == null || objectName.isBlank()) return;
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("Đã xóa file thành công: {}", objectName);
        } catch (Exception e) {
            log.error("Không thể xóa file trên Minio: {}", objectName);
        }
    }

    private String executeUpload(MultipartFile file, String folder) {
        try {
            // Cấu trúc: folder/uuid/filename (Bạn đã dùng cấu trúc này, rất tốt để tránh trùng tên)
            String fileName = folder + "/" + UUID.randomUUID() + "/" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            // set 20MB , tự chia file lớn thành file nhỏ nhiều phần để load đữ liệu lên
                            .stream(file.getInputStream(), file.getSize(), 10 * 1024 * 1024)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("Upload thành công: {}", fileName);
            return fileName;
        } catch (Exception e) {
            log.error("Lỗi thực thi upload Minio: {}", e.getMessage());
            throw new RuntimeException("Lỗi hệ thống lưu trữ: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file, long maxSize, List<String> allowedTypes, ErrorCode typeErrorCode) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }

        // Kiểm tra dung lượng
        if (file.getSize() > maxSize) {
            throw new AppException(maxSize > 10 * 1024 * 1024 ? ErrorCode.VIDEO_TOO_LARGE : ErrorCode.FILE_TOO_LARGE);
        }

        // Kiểm tra định dạng
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new AppException(typeErrorCode);
        }
    }
}