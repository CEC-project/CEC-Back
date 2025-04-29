package com.backend.server.api.user.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.dto.equipment.EquipmentListRequest;
import com.backend.server.api.user.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.dto.equipment.FavoriteListResponse;
import com.backend.server.api.user.dto.equipment.EquipmentRentalListRequest;
import com.backend.server.api.user.dto.equipment.EquipmentRentalListResponse;
import com.backend.server.api.user.dto.equipment.EquipmentRentalRequest;
import com.backend.server.api.user.dto.equipment.EquipmentRentalResponse;
import com.backend.server.api.user.service.EquipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment API", description = "장비 API")
public class EquipmentController {

    private final EquipmentService EquipmentService;

    @GetMapping
    @Operation(
        summary = "장비 목록 조회",
        description = "장비 목록을 카테고리, 상태, 대여 가능 여부, 검색어, 사용자 학년 및 장바구니 필터링 조건에 따라 조회합니다. 그리고 즐겨찾기를 곁들인 근데 왜 밑에 또 즐겨찾기 api가 있냐면 그냥 만들어봤어요 혹시몰라서서"
    )    
    public ApiResponse<EquipmentListResponse> getEquipments(EquipmentListRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("장비 목록 조회 성공", EquipmentService.getEquipments(loginUser, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "장비 상세 조회", description = "장비 상세 정보를 조회합니다.")
    public ApiResponse<EquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세 조회 성공", EquipmentService.getEquipment(id));
    }

    @PostMapping("/rent-request")
    @Operation(summary = "장비 대여 요청", description = "단일 장비 대여를 요청합니다.")
    public ApiResponse<EquipmentRentalResponse> createRentRequest(@RequestBody EquipmentRentalRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        EquipmentRentalResponse response = EquipmentService.createRentRequest(loginUser, request);
        return ApiResponse.success("장비 대여 요청 성공", response);
    }
    
    @PostMapping("/rent-requests")
    @Operation(summary = "다중 장비 대여 요청", description = "여러 장비의 대여를 한번에 요청합니다.")
    public ApiResponse<EquipmentRentalListResponse> createRentRequests(@RequestBody EquipmentRentalListRequest requestList, @AuthenticationPrincipal LoginUser loginUser) {
        EquipmentRentalListResponse responses = EquipmentService.createRentRequests(loginUser, requestList);
        return ApiResponse.success("다중 장비 대여 요청 성공", responses);
    }
    
    @PostMapping("/return-request")
    @Operation(summary = "장비 반납 요청", description = "장비 반납을 요청합니다.")
    public ApiResponse<EquipmentRentalResponse> createReturnRequest(@RequestBody EquipmentRentalRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        EquipmentRentalResponse response = EquipmentService.createReturnRequest(loginUser, request);
        return ApiResponse.success("장비 반납 요청 성공", response);
    }
    
    @PostMapping("/return-requests")
    @Operation(summary = "다중 장비 반납 요청", description = "여러 장비의 반납을 한번에 요청합니다.")
    public ApiResponse<EquipmentRentalListResponse> createReturnRequests(@RequestBody EquipmentRentalListRequest requestList, @AuthenticationPrincipal LoginUser loginUser) {
        EquipmentRentalListResponse responses = EquipmentService.createReturnRequests(loginUser, requestList);
        return ApiResponse.success("다중 장비 반납 요청 성공", responses);
    }
    
    @PostMapping("/cancel-request/{requestId}")
    @Operation(summary = "대여/반납 요청 취소", description = "대여 또는 반납 요청을 취소합니다.")
    public ApiResponse<Void> cancelRentalRequest(@PathVariable Long requestId) {
        EquipmentService.cancelRentalRequest(requestId);
        return ApiResponse.success("요청 취소 성공", null);
    }
    
    @PostMapping("/cancel-requests")
    @Operation(summary = "다중 대여/반납 요청 취소", description = "여러 개의 대여 또는 반납 요청을 한번에 취소합니다.")
    public ApiResponse<List<Long>> cancelBulkRentalRequests(@RequestBody List<Long> requestIds) {
        EquipmentService.cancelBulkRentalRequests(requestIds);
        return ApiResponse.success("다중 요청 취소 성공", requestIds);
    }
    
    @GetMapping("/favorites")
    @Operation(summary = "즐겨찾기 장비 목록 조회", description = "즐겨찾기에 추가된 장비 목록을 조회합니다.")
    public ApiResponse<FavoriteListResponse> getFavorites(EquipmentListRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("즐겨찾기 장비 목록 조회 성공", EquipmentService.getFavoriteList(loginUser, request));
    }
    
    @PostMapping("/favorite/{equipmentId}")
    @Operation(summary = "즐겨찾기 추가", description = "장비를 즐겨찾기에 추가합니다.")
    public ApiResponse<Void> addFavorite(@PathVariable Long equipmentId, @AuthenticationPrincipal LoginUser loginUser) {
        EquipmentService.addFavorite(equipmentId, loginUser);
        return ApiResponse.success("즐겨찾기 추가 성공", null);
    }
    
    @DeleteMapping("/favorite/{equipmentId}")
    @Operation(summary = "즐겨찾기 삭제", description = "장비를 즐겨찾기에서 삭제합니다.")
    public ApiResponse<Void> removeFavorite(@PathVariable Long equipmentId, @AuthenticationPrincipal LoginUser loginUser) {
        EquipmentService.removeFavorite(equipmentId, loginUser);
        return ApiResponse.success("즐겨찾기 삭제 성공", null);
    }
} 