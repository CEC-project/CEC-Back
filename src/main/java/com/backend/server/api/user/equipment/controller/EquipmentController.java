package com.backend.server.api.user.equipment.controller;

import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentRentalRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.equipment.service.EquipmentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Tag(name = "1-3. 대여 신청 / 장비", description = "수정 필요")
@RestController
@RequestMapping("/api/user/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 단일 조회"
            )
    public ApiResponse<EquipmentResponse> getEquipment(
            @Parameter(description = "장비 ID", required = true) @PathVariable Long id) {
        return ApiResponse.success("장비 조회 성공", equipmentService.getEquipment(id));
    }

    @GetMapping
    @Operation(
            summary = "장비 목록 조회 (내가 대여한 장비 목록 보기 이걸로 가능)"
    )
    public ApiResponse<EquipmentListResponse> getEquipments(
            @ParameterObject @ModelAttribute EquipmentListRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("장비 목록 조회 성공", equipmentService.getEquipments(loginUser, request));
    }

    @PostMapping("/cart")
    @Operation(
            summary = "장비 장바구니 추가"
    )
    public ApiResponse<Void> addToCart(
            @RequestBody List<Long> equipmentIds,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.addToCart(loginUser, equipmentIds);
        return ApiResponse.success("장바구니 추가 성공", null);
    }

    @GetMapping("/cart")
    @Operation(
            summary = "장비 장바구니 조회"
    )
    public ApiResponse<List<EquipmentResponse>> getCartItems(
            @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("장바구니 조회 성공", equipmentService.getCartItems(loginUser));
    }

    @PatchMapping("/rental")
    @Operation(
            summary = "장비 대여 요청 - 대여 성공시 장바구니에 있는 장비 자동 삭제"
    )
    public ApiResponse<Void> requestRental(
            @RequestBody EquipmentRentalRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.requestRental(loginUser, request);
        return ApiResponse.success("대여 요청 성공", null);
    }

    @PatchMapping("/rental/cancel")
    @Operation(
            summary = "장비 대여 요청 취소"
            )
    public ApiResponse<Void> cancelRentalRequest(
            @RequestBody List<Long> equipmentIds,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.cancelRentalRequest(loginUser, equipmentIds);
        return ApiResponse.success("대여 요청 취소 성공", null);
    }

    @PatchMapping("/return")
    @Operation(
            summary = "장비 반납 요청"
    )
    public ApiResponse<Void> requestReturn(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody List<Long> equipmentIds) {
        equipmentService.requestReturn(loginUser, equipmentIds);
        return ApiResponse.success("반납 요청 성공", null);
    }

    @PatchMapping("/return/cancel")
    @Operation(
            summary = "장비 반납 요청 취소"
            )
    public ApiResponse<Void> cancelReturnRequest(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody List<Long> equipmentIds) {
        equipmentService.cancelReturnRequest(loginUser, equipmentIds);
        return ApiResponse.success("반납 요청 취소 성공", null);
    }


}
