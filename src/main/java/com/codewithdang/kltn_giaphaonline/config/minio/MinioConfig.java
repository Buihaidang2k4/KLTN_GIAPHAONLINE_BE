package com.codewithdang.kltn_giaphaonline.config.minio;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {
    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        try {
            client.listBuckets();
            log.info("Connected MinIO Successfully");
            log.error("MinIO URL: {}", url);
            log.error("MinIO Access Key: {}", accessKey);
        } catch (Exception e) {
            log.error("Cannot connect MinIO", e);
            log.error("MinIO URL: {}", url);
        }

        return client;
    }
}
