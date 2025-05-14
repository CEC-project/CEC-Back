package com.backend.server.api.user.equipment.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentRentalRequest;
import com.backend.server.api.user.equipment.service.EquipmentService;
import com.backend.server.model.entity.Equipment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Tag(name = "장비 관리", description = "장비 조회, 대여, 반납 관련 API")
@RestController
@RequestMapping("/api/user/equipments")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @Operation(
        summary = "장비 개별 조회", 
        description = "장비 ID를 이용하여 단일 장비의 상세 정보를 조회합니다.\n\n" +
                      "예시 URL: http://localhost:8080/api/user/equipments/1"
    )
    @GetMapping("/{id}")
    public ApiResponse<EquipmentResponse> getEquipment(
            @Parameter(description = "장비 ID", required = true) @PathVariable Long id) {
        return ApiResponse.success("장비 조회 성공", equipmentService.getEquipment(id));
    }

    @Operation(
        summary = "장비 목록 조회", 
        description = "다양한 검색, 정렬, 필터 조건으로 장비 목록을 조회합니다.\n\n" +
                      "**검색/필터 파라미터:**\n" +
                      "- categoryId: 장비 분류(카테고리) ID\n" +
                      "- modelName: 모델명(부분 일치)\n" +
                      "- renterName: 현재 대여자 이름(부분 일치)\n" +
                      "- isAvailable: 대여 가능 여부(true/false)\n" +
                      "- searchKeyword: 통합 검색(모델명, 일련번호 등)\n\n" +
                      "**정렬 파라미터:**\n" +
                      "- sortBy: 정렬 기준(id, createdAt 등)\n" +
                      "- sortDirection: 정렬 방향(asc, desc)\n\n" +
                      "**페이징 파라미터:**\n" +
                      "- page: 페이지 번호(0부터 시작)\n" +
                      "- size: 한 페이지에 보여줄 개수\n\n" +
                      "**예시 URL:**\n" +
                      "- 기본 조회: http://localhost:8080/api/user/equipments\n" +
                      "- 필터링 조회: http://localhost:8080/api/user/equipments?categoryId=1&isAvailable=true\n" +
                      "- 검색 조회: http://localhost:8080/api/user/equipments?searchKeyword=맥북\n" +
                      "- 정렬 및 페이징: http://localhost:8080/api/user/equipments?page=0&size=10&sortBy=id&sortDirection=desc"
    )
    @GetMapping
    public ApiResponse<EquipmentListResponse> getEquipments(
            @Parameter(description = "장비 목록 필터링 조건") @ModelAttribute EquipmentListRequest request,
            @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("장비 목록 조회 성공", equipmentService.getEquipments(loginUser, request));
    }

    @Operation(
        summary = "장비 장바구니 추가", 
        description = "선택한 장비들을 사용자의 장바구니에 추가합니다. 이미 장바구니에 있는 장비는 무시됩니다.\n\n" +
                      "**요청 예시:**\n" +
                      "```\n[1, 2, 3]\n```\n\n" +
                      "**예시 URL:** http://localhost:8080/api/user/equipments/cart"
    )
    @PostMapping("/cart")
    public ApiResponse<Void> addToCart(
            @Parameter(description = "장바구니에 추가할 장비 ID 목록") @RequestBody List<Long> equipmentIds,
            @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.addToCart(loginUser, equipmentIds);
        return ApiResponse.success("장바구니 추가 성공", null);
    }

    @Operation(
        summary = "장비 장바구니 조회", 
        description = "사용자의 장바구니에 있는 장비 목록을 조회합니다.\n\n" +
                      "**예시 URL:** http://localhost:8080/api/user/equipments/cart?userId=1"
    )
    @GetMapping("/cart")
    public ApiResponse<List<EquipmentResponse>> getCartItems(
            @Parameter(description = "사용자 ID") @RequestParam Long userId) {
        return ApiResponse.success("장바구니 조회 성공", equipmentService.getCartItems(userId));
    }

    @Operation(
        summary = "장비 대여 요청", 
        description = "장비 대여를 요청합니다. 대여 기간을 지정해야 합니다. 장비가 대여 가능한 상태인 경우에만 대여 요청이 성공합니다.\n\n" +
                      "**요청 예시:**\n" +
                      "```\n{\n  \"equipmentIds\": [1, 2, 3],\n  \"startDate\": \"2023-06-01T09:00:00\",\n  \"endDate\": \"2023-06-10T18:00:00\"\n}\n```\n\n" +
                      "**예시 URL:** http://localhost:8080/api/user/equipments/rental"
    )
    @PostMapping("/rental")
    public ApiResponse<Void> requestRental(
            @Parameter(description = "장비 대여 요청 정보") @RequestBody EquipmentRentalRequest request,
            @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.requestRental(loginUser, request);
        return ApiResponse.success("대여 요청 성공", null);
    }

    @Operation(
        summary = "장비 대여 요청 취소", 
        description = "대여 요청 상태인 장비의 대여 요청을 취소합니다. 본인이 요청한 대여만 취소할 수 있습니다.\n\n" +
                      "**요청 예시:**\n" +
                      "```\n[1, 2, 3]\n```\n\n" +
                      "**예시 URL:** http://localhost:8080/api/user/equipments/rental/cancel"
    )
    @PostMapping("/rental/cancel")
    public ApiResponse<Void> cancelRentalRequest(
            @Parameter(description = "대여 요청 취소할 장비 ID 목록") @RequestBody List<Long> equipmentIds,
            @Parameter(description = "로그인 유저 정보") @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.cancelRentalRequest(loginUser, equipmentIds);
        return ApiResponse.success("대여 요청 취소 성공", null);
    }

    @Operation(
        summary = "장비 반납 요청", 
        description = "대여 중인 장비의 반납을 요청합니다. 본인이 대여한 장비만 반납 요청할 수 있습니다.\n\n" +
                      "**요청 예시:**\n" +
                      "```\n[1, 2, 3]\n```\n\n" +
                      "**예시 URL:** http://localhost:8080/api/user/equipments/return?userId=1"
    )
    @PostMapping("/return")
    public ApiResponse<Void> requestReturn(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @Parameter(description = "반납 요청할 장비 ID 목록") @RequestBody List<Long> equipmentIds) {
        equipmentService.requestReturn(userId, equipmentIds);
        return ApiResponse.success("반납 요청 성공", null);
    }

    @Operation(
        summary = "장비 반납 요청 취소", 
        description = "반납 요청 상태인 장비의 반납 요청을 취소합니다. 본인이 요청한 반납만 취소할 수 있습니다.\n\n" +
                      "**요청 예시:**\n" +
                      "```\n[1, 2, 3]\n```\n\n" +
                      "**예시 URL:** http://localhost:8080/api/user/equipments/return/cancel?userId=1"
    )
    @PostMapping("/return/cancel")
    public ApiResponse<Void> cancelReturnRequest(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @Parameter(description = "반납 요청 취소할 장비 ID 목록") @RequestBody List<Long> equipmentIds) {
        equipmentService.cancelReturnRequest(userId, equipmentIds);
        return ApiResponse.success("반납 요청 취소 성공", null);
    }

    // // 내 대여 목록 조회
    // @GetMapping("/my-rentals")
    // public ApiResponse<List<Equipment>> getMyRentals(@RequestParam Long userId) {
    //     return ApiResponse.success("내 대여 목록 조회 성공", equipmentService.getMyRentals(userId));
    // }
}
