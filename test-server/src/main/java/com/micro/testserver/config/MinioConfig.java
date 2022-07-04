package com.micro.testserver.config;

import com.micro.commonserver.service.MinioService;
import io.minio.errors.MinioException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
@Component
public class MinioConfig {

    @Bean
    public MinioService minioTemplate(@Value("${minio.endPoint}") String endPoint, @Value("${minio.accessKey}") String accessKey,
                                      @Value("${minio.secretKey}") String secretKey, @Value("${minio.partSize}") int partSize) throws MinioException {
        return new MinioService(endPoint, accessKey, secretKey, partSize);
    }
}