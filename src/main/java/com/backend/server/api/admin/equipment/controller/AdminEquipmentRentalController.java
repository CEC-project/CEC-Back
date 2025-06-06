package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentDetailRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.ExtendRentalPeriodsRequest;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentRentalService;
import com.backend.server.api.admin.equipment.service.AdminEquipmentService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/equipments-rental")
@Tag(name = "4-1. 대여 신청/반납 관리 / 장비 관리", description = "수정 완료")
public class AdminEquipmentRentalController {
    private final AdminEquipmentService adminEquipmentService;
    private final AdminEquipmentRentalService adminEquipmentRentalService;

    @GetMapping
    @Operation(
            summary = "장비 목록 조회"

    )
    public ApiResponse<AdminEquipmentListResponse> getEquipments(
            @ParameterObject AdminEquipmentListRequest request) {
        return ApiResponse.success("장비 리스트 조회 성공", adminEquipmentService.getEquipments(request));
    }

    @Operation(
            summary = "장비 대여 일괄 상태 변경 API" , description = "상태는 [RETURN, CANCEL, BROKEN, REJECT, ACCEPT] 중 선택, ACCEPT,RETURN일 경우 detail생략")
    @PatchMapping("/status")
    public ApiResponse<List<Long>> changeStatus(@Valid @RequestBody AdminEquipmentDetailRequest request) {
        List<Long> ids = adminEquipmentRentalService.changeStatus(request);
        return ApiResponse.success("장비의 상태가 일괄 변경되었습니다.", ids);
    }
}
