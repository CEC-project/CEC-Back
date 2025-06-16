package com.backend.server.api.user.history.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.history.dto.RentalRestrictionListRequest;
import com.backend.server.api.user.history.dto.RentalRestrictionListResponse;
import com.backend.server.api.user.history.service.RentalRestrictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2-2. 대여 내역 / 대여 제재 내역", description = "수정 완료")
@RestController
@RequestMapping("/api/history/rental-restriction")
@RequiredArgsConstructor
public class RentalRestrictionController {

    private final RentalRestrictionService rentalRestrictionService;

    @GetMapping
    @Operation(summary = "자기가 제재당한 내역 목록 조회")
    public ApiResponse<RentalRestrictionListResponse> getRentalRestrictionList(
            @ParameterObject RentalRestrictionListRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        return ApiResponse.success("자기가 제재당한 내역 목록 조회 성공",
                rentalRestrictionService.getRentalRestrictionList(request, loginUser));
    }
}
