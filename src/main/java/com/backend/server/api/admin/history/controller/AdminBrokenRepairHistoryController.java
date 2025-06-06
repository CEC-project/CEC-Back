package com.backend.server.api.admin.history.controller;

import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryListResponse;
import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryRequest;
import com.backend.server.api.admin.history.service.AdminBrokenRepairHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/broken-repair-history")
@Tag(name = "관리자 수리/파손 이력", description = "수리/파손 이력 관리 API")
public class AdminBrokenRepairHistoryController {

    private final AdminBrokenRepairHistoryService adminBrokenRepairHistoryService;

    @GetMapping
    @Operation(summary = "수리/파손 이력 목록 조회")
    public AdminBrokenRepairHistoryListResponse getBrokenRepairHistory(@ParameterObject AdminBrokenRepairHistoryRequest request) {
        return adminBrokenRepairHistoryService.getBrokenRepairHistory(request);
    }
}
