package com.backend.server.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    @Value("${cloud.aws.credentials.access-key:asdf}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:asdf}")
    private String secretKey;

    private final S3Properties s3Properties;

    @Bean
    public AwsBasicCredentials credentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean(destroyMethod = "close")
    public S3Presigner getS3Presigner(AwsBasicCredentials credentials) {
        return S3Presigner.builder()
                .region(Region.of(s3Properties.region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public S3Client s3Client(AwsBasicCredentials credentials) {
        return S3Client.builder()
                .region(Region.of(s3Properties.region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Getter
    @Component
    public static class S3Properties {
        @Value("${cloud.aws.s3.bucket:asdf}")
        private String bucket;

        @Value("${cloud.aws.region.static:asdf}")
        private String region;
    }
}
