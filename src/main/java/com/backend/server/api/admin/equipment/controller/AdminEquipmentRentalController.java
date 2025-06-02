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
@Tag(name = "장비 대여 관리 API", description = "관리자 권한으로 대여에 관한 항목을 처리하는 API입니다.")
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
            summary = "장비 대여 일괄 상태 변경 API",
            description = """
            - **요청 바디**
              - **equipmentIds** : 필수, 숫자 배열
              - **status** : 필수, RETURN, REJECT, BROKEN, ACCEPT
              - **detail** : 생략 가능, 100자 이내""")
    @PatchMapping("/status")
    public ApiResponse<List<Long>> changeStatus(@Valid @RequestBody AdminEquipmentDetailRequest request) {
        List<Long> ids = adminEquipmentRentalService.changeStatus(request);
        return ApiResponse.success("장비의 상태가 일괄 변경되었습니다.", ids);
    }



    @PostMapping("/extend")
    @Operation(
            summary = "대여 기간 연장",
            description = "대여 중인 장비들의 반납 기한을 새로운 날짜로 연장합니다."
    )
    public ApiResponse<Void> extendRentalPeriods(
            @RequestBody ExtendRentalPeriodsRequest request) {
        adminEquipmentRentalService.extendRentalPeriods(request.getEquipmentIds(), request.getNewEndDate());
        return ApiResponse.success("대여 기간 연장 성공", null);
    }


}
