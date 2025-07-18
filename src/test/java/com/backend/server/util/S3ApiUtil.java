package com.backend.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.backend.server.config.S3Config.S3Properties;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Component
public class S3ApiUtil {

    @Autowired RestTemplate restTemplate;
    @Autowired S3Client s3Client;
    @Autowired S3Properties s3Properties;

    public void upload(String presignedUrl, String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(content.getBytes(StandardCharsets.UTF_8).length);

        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(presignedUrl),
                HttpMethod.PUT,
                new HttpEntity<>(content, headers),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public ResponseEntity<String> get(String fileKey) {
        final String url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3Properties.getBucket(), s3Properties.getRegion(), fileKey);

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    String.class
            );
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void delete(String fileKey) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(fileKey)
                .build();
        s3Client.deleteObject(request);
    }
}
