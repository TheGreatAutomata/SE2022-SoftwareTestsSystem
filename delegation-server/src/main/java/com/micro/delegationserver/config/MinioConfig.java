package com.micro.delegationserver.config;

import com.micro.delegationserver.service.MinioServce;
import io.minio.errors.MinioException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Jarvis
 * @date 2020/1/26 11:38
 */
@Configuration
@Data
@Component
public class MinioConfig {

    @Bean
    public MinioServce minioTemplate(@Value("${minio.endPoint}") String endPoint, @Value("${minio.accessKey}") String accessKey,
                                     @Value("${minio.secretKey}") String secretKey, @Value("${minio.partSize}") int partSize) throws MinioException {
        return new MinioServce(endPoint, accessKey, secretKey, partSize);
    }
}
