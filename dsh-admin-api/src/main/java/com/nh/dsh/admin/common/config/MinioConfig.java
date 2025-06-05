package com.nh.dsh.admin.common.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
 

/**
 * @author Flobby
 */
@Data
@Component
public class MinioConfig {
 
    @Value(value = "${minio.bucket}")
    private String bucket;
 
    @Value(value = "${minio.host}")
    private String host;
 
    @Value(value = "${minio.access-key}")
    private String accessKey;
 
    @Value(value = "${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient(){

        return MinioClient.builder()
                .endpoint(host)
                .credentials(accessKey, secretKey)
                .build();
    }
}
 