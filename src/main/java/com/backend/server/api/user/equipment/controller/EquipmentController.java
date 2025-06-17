package com.backend.server.api.user.equipment.controller;

import com.backend.server.api.user.equipment.dto.equipment.*;
import com.backend.server.api.user.equipment.service.EquipmentService;
import com.backend.server.model.entity.enums.EquipmentAction;
import jakarta.validation.Valid;

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

@Tag(name = "1-3. 대여 신청 / 장비", description = "수정 완료")
@RestController
@RequestMapping("/api/equipments")
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
            summary = "장비 목록 조회"
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
            @RequestBody EquipmentCartListRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.addToCart(loginUser, request);
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

    @DeleteMapping("/cart")
    @Operation(
            summary = "장비 장바구니 삭제"
    )
    public ApiResponse<List<Long>> deleteCartItems(
            @AuthenticationPrincipal LoginUser loginUser, @RequestBody EquipmentCartListRequest request) {
        return ApiResponse.success("장바구니 삭제 성공", equipmentService.deleteCartItemsByEquipmentIds(loginUser, request));
    }




    @PatchMapping("/action")
    @Operation(summary = "장비 상태 변경 요청 (대여/반납 요청 및 취소)",description = "날짜는 대여 요청 시에만 필요")
    public ApiResponse<Void> handleEquipmentAction(
            @Valid
            @RequestBody EquipmentActionRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        equipmentService.handleUserAction(loginUser, request);
        return ApiResponse.success("장비 처리 완료", null);
    }
}
