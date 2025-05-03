package com.backend.server.api.common.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.service.CommonPresignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class CommonPresignedUrlController {

    private final CommonPresignedUrlService presignedUrlService;

    @Operation(
            summary = "s3에 파일 업로드용 presigned url 발급 API",
            description = "presigned url 을 발급합니다. presigned url 은 5분만 유효합니다.<br/>"
                    + "contentType 은 png 이미지를 업로드 한다면 image/png 로 설정하면 됩니다.<br/>"
                    + "어떤 파일인지 모를때는 contentType 을 기본값으로 보내도 됩니다.<br/><br/>"
                    + "아래는 curl 명령어로 파일을 업로드하는 예시입니다.<br/>"
                    + "curl -X PUT \"발급받은 url\" -H \"Content-Type: 파일의 타입명\" --upload-file 파일 위치<br/><br/>"
                    + "아래는 js fetch 명령어로 파일을 업로드하는 예시입니다.<br/>"
                    + "fetch(발급받은 url, { method: 'PUT', headers: { 'Content-Type': 파일의 타입명 }, body: file });<br/>"
                    + "(file 객체는 &lt;input type=\"file\"&gt; 등으로 얻은 File 타입 객체입니다.)"
    )
    @GetMapping("/presigned-url")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ApiResponse<String> getPresignedUrl(
            @RequestParam String fileName,
            @RequestParam(defaultValue = "application/octet-stream") String contentType) {
        String url = presignedUrlService.generatePresignedUrl(fileName, contentType);
        return ApiResponse.success("presigned url 발급 성공", url);
    }
}