package com.backend.server.api.common.s3.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.s3.service.CommonPresignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
@Tag(name = "파일 업로드 API", description = "presigned url 발급 API")
public class CommonPresignedUrlController {

    private final CommonPresignedUrlService presignedUrlService;

    @Operation(
            summary = "s3에 파일 업로드용 presigned url 발급 API",
            description = "presigned url 을 발급합니다. presigned url 은 5분만 유효합니다.<br/><br/>"
                    + "아래는 curl 명령어로 파일을 업로드하는 예시입니다.<br/>"
                    + "curl -X PUT \"발급받은 url\" --upload-file 파일 위치<br/><br/>"
                    + "아래는 js fetch 명령어로 파일을 업로드하는 예시입니다.<br/>"
                    + "fetch(발급받은 url, { method: 'PUT', body: 파일 객체 });<br/>"
                    + "(파일 객체는 &lt;input type=\"file\"&gt; 등으로 얻은 File 타입 객체입니다.)"
    )
    @GetMapping("/presigned-url")
    public ApiResponse<String> getPresignedUrl(
            @RequestParam String fileName) {
        String url = presignedUrlService.generatePresignedUrl(fileName);
        return ApiResponse.success("presigned url 발급 성공", url);
    }
}