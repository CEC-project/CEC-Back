package com.backend.server.api.common.healthCheck;

import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check")
@Tag(name = "서버 상태 체크용 API")
public class CommonHealthCheckController {

    private final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @Operation(summary = "서버가 정상이면 무조건 성공하는 api 입니다.")
    @GetMapping
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("헬스 체크 성공", now);
    }
}