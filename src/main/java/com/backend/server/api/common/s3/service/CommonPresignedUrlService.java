package com.backend.server.api.common.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommonPresignedUrlService {

    @Value("${cloud.aws.s3.bucket:1111}")
    private String bucketName;

    @Value("${cloud.aws.credentials.access-key:1111}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:1111}")
    private String secretKey;

    @Value("${cloud.aws.region.static:1111}")
    private String region;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public String generatePresignedUrl(String fileName, String contentType) {
        // 만료시간: 현재 시간 + 5분
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 5);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);
        request.addRequestParameter("Content-Type", contentType);

        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }
}