package com.backend.server.api.user.history.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.history.dto.RentalHistoryListRequest;
import com.backend.server.api.user.history.dto.RentalHistoryListResponse;
import com.backend.server.api.user.history.service.RentalHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2-1. 대여 내역 / 대여 내역", description = "수정 완료")
@RestController
@RequestMapping("/api/history/rental")
@RequiredArgsConstructor
public class RentalHistoryController {

    private final RentalHistoryService rentalHistoryService;

    @GetMapping
    @Operation(summary = "장비 및 강의실 대여 목록 조회")
    public ApiResponse<RentalHistoryListResponse> getRentalHistoryList(
            @ParameterObject RentalHistoryListRequest request
    ) {
        return ApiResponse.success("장비 및 강의실 대여 목록 조회 성공",
                rentalHistoryService.getRentalHistoryList(request));
    }
}
