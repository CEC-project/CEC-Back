package com.backend.server.api.user.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.dto.EquipmentListRequest;
import com.backend.server.api.user.dto.EquipmentListResponse;
import com.backend.server.api.user.dto.EquipmentResponse;
import com.backend.server.api.user.dto.RentalResponse;
import com.backend.server.api.user.dto.FavoriteListResponse;

import com.backend.server.api.user.dto.RentalListResponse;
import com.backend.server.api.user.dto.RentalRequest;
import com.backend.server.api.user.dto.RentalListRequest;

import com.backend.server.api.user.service.EquipmentService;
import com.backend.server.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment API", description = "장비 API")
public class EquipmentController {

    private final EquipmentService EquipmentService;

    @GetMapping
    @Operation(summary = "장비 목록 조회", description = "장비 목록을 조회합니다. sortedby 0 1 2 |  (name|category|status)")
    public ApiResponse<EquipmentListResponse> getEquipments(EquipmentListRequest request) {
        
        return ApiResponse.success("장비 목록 조회 성공", EquipmentService.getEquipments(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "장비 상세 조회", description = "장비 상세 정보를 조회합니다.")
    public ApiResponse<EquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세 조회 성공", EquipmentService.getEquipment(id));
    }

    @PostMapping("/rent-request")
    @Operation(summary = "장비 대여 요청", description = "단일 장비 대여를 요청합니다.")
    public ApiResponse<RentalResponse> createRentRequest(@RequestBody RentalRequest request) {
        RentalResponse response = EquipmentService.createRentRequest(request);
        return ApiResponse.success("장비 대여 요청 성공", response);
    }
    
    @PostMapping("/rent-requests")
    @Operation(summary = "다중 장비 대여 요청", description = "여러 장비의 대여를 한번에 요청합니다.")
    public ApiResponse<RentalListResponse> createRentRequests(@RequestBody RentalListRequest requestList) {
        RentalListResponse responses = EquipmentService.createRentRequests(requestList);
        return ApiResponse.success("다중 장비 대여 요청 성공", responses);
    }
    
    @PostMapping("/return-request")
    @Operation(summary = "장비 반납 요청", description = "장비 반납을 요청합니다.")
    public ApiResponse<RentalResponse> createReturnRequest(@RequestBody RentalRequest request) {
        RentalResponse response = EquipmentService.createReturnRequest(request);
        return ApiResponse.success("장비 반납 요청 성공", response);
    }
    
    @PostMapping("/return-requests")
    @Operation(summary = "다중 장비 반납 요청", description = "여러 장비의 반납을 한번에 요청합니다.")
    public ApiResponse<RentalListResponse> createReturnRequests(@RequestBody RentalListRequest requestList) {
        RentalListResponse responses = EquipmentService.createReturnRequests(requestList);
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
    public ApiResponse<FavoriteListResponse> getFavorites(EquipmentListRequest request) {
        return ApiResponse.success("즐겨찾기 장비 목록 조회 성공", EquipmentService.getFavoriteList(request));
    }
    
    @PostMapping("/favorite/{equipmentId}")
    @Operation(summary = "즐겨찾기 추가", description = "장비를 즐겨찾기에 추가합니다.")
    public ApiResponse<Void> addFavorite(@PathVariable Long equipmentId) {
        EquipmentService.addFavorite(equipmentId);
        return ApiResponse.success("즐겨찾기 추가 성공", null);
    }
    
    @DeleteMapping("/favorite/{equipmentId}")
    @Operation(summary = "즐겨찾기 삭제", description = "장비를 즐겨찾기에서 삭제합니다.")
    public ApiResponse<Void> removeFavorite(@PathVariable Long equipmentId) {
        EquipmentService.removeFavorite(equipmentId);
        return ApiResponse.success("즐겨찾기 삭제 성공", null);
    }
} 