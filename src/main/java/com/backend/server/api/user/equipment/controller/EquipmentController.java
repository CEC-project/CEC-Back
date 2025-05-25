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

@Tag(name = "장비 관리", description = "사용자 장비 조회, 대여, 반납 기능을 제공하는 API입니다.")
@RestController
@RequestMapping("/api/user/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping("/{id}")
    @Operation(
            summary = "장비 개별 조회",
            description = """
        장비 ID를 이용하여 단일 장비의 상세 정보를 조회합니다.

        예시 요청:
        - `/api/user/equipments/1`

        반환값에는 모델명, 일련번호, 상태, 대여 가능 여부 등 상세 정보가 포함됩니다.
        """
    )
    public ApiResponse<EquipmentResponse> getEquipment(
            @Parameter(description = "장비 ID", required = true) @PathVariable Long id) {
        return ApiResponse.success("장비 조회 성공", equipmentService.getEquipment(id));
    }

    @GetMapping
    @Operation(
            summary = "장비 목록 조회",
            description = """
        검색, 필터링, 정렬, 페이징 조건에 따라 장비 목록을 조회합니다.

        필터 조건:
        - `categoryId` (Long): 장비 카테고리 ID
        - `modelName` (String): 모델명 (부분 일치)
        - `renterName` (String): 현재 대여자 이름 (부분 일치)
        - `isAvailable` (Boolean): 대여 가능 여부
        - `searchKeyword` (String): 모델명, 일련번호, 대여자 통합 검색

        정렬 조건:
        - `sortBy` (String): 정렬 기준 필드 (예: id, createdAt)
        - `sortDirection` (String): 정렬 방향 (asc 또는 desc)

        페이징 조건:
        - `page` (Integer): 페이지 번호 (0부터 시작)
        - `size` (Integer): 한 페이지당 항목 수

        예시 요청:
        - `/api/user/equipments?categoryId=1&isAvailable=true`
        - `/api/user/equipments?searchKeyword=맥북&page=0&size=10&sortBy=id&sortDirection=desc`
        """
    )
    public ApiResponse<EquipmentListResponse> getEquipments(
            @ModelAttribute EquipmentListRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("장비 목록 조회 성공", equipmentService.getEquipments(loginUser, request));
    }

    @PostMapping("/cart")
    @Operation(
            summary = "장비 장바구니 추가",
            description = """
        장비를 장바구니에 추가합니다. 이미 추가된 장비는 무시됩니다.

        요청 형식:
        ```
        [1, 2, 3]
        ```

        예시 요청:
        - POST `/api/user/equipments/cart`
        """
    )
    public ApiResponse<Void> addToCart(
            @RequestBody List<Long> equipmentIds,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.addToCart(loginUser, equipmentIds);
        return ApiResponse.success("장바구니 추가 성공", null);
    }

    @GetMapping("/cart")
    @Operation(
            summary = "장비 장바구니 조회",
            description = """
        현재 로그인한 사용자의 장바구니에 추가된 장비 목록을 조회합니다.

        예시 요청:
        - GET `/api/user/equipments/cart`
        """
    )
    public ApiResponse<List<EquipmentResponse>> getCartItems(
            @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("장바구니 조회 성공", equipmentService.getCartItems(loginUser));
    }

    @PostMapping("/rental")
    @Operation(
            summary = "장비 대여 요청",
            description = """
        장비 대여를 요청합니다. 장비가 AVAILABLE 상태여야 하며, 요청 시 대여 기간을 지정해야 합니다.

        요청 형식:
        ```
        {
          "equipmentIds": [1, 2, 3],
          "startDate": "2023-06-01T09:00:00",
          "endDate": "2023-06-10T18:00:00"
        }
        ```

        예시 요청:
        - POST `/api/user/equipments/rental`
        """
    )
    public ApiResponse<Void> requestRental(
            @RequestBody EquipmentRentalRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.requestRental(loginUser, request);
        return ApiResponse.success("대여 요청 성공", null);
    }

    @PostMapping("/rental/cancel")
    @Operation(
            summary = "장비 대여 요청 취소",
            description = """
        RENTAL_PENDING 상태인 장비의 대여 요청을 취소합니다. 본인이 요청한 장비만 취소할 수 있습니다.

        요청 형식:
        ```
        [1, 2, 3]
        ```

        예시 요청:
        - POST `/api/user/equipments/rental/cancel`
        """
    )
    public ApiResponse<Void> cancelRentalRequest(
            @RequestBody List<Long> equipmentIds,
            @AuthenticationPrincipal LoginUser loginUser) {
        equipmentService.cancelRentalRequest(loginUser, equipmentIds);
        return ApiResponse.success("대여 요청 취소 성공", null);
    }

    @PostMapping("/return")
    @Operation(
            summary = "장비 반납 요청",
            description = """
        사용 중인 장비를 반납 요청합니다. 본인이 대여한 장비만 요청할 수 있습니다.

        요청 형식:
        ```
        [1, 2, 3]
        ```

        예시 요청:
        - POST `/api/user/equipments/return`
        """
    )
    public ApiResponse<Void> requestReturn(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody List<Long> equipmentIds) {
        equipmentService.requestReturn(loginUser, equipmentIds);
        return ApiResponse.success("반납 요청 성공", null);
    }

    @PostMapping("/return/cancel")
    @Operation(
            summary = "장비 반납 요청 취소",
            description = """
        RETURN_PENDING 상태의 반납 요청을 취소합니다. 본인이 요청한 장비만 가능합니다.

        요청 형식:
        ```
        [1, 2, 3]
        ```

        예시 요청:
        - POST `/api/user/equipments/return/cancel`
        """
    )
    public ApiResponse<Void> cancelReturnRequest(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody List<Long> equipmentIds) {
        equipmentService.cancelReturnRequest(loginUser, equipmentIds);
        return ApiResponse.success("반납 요청 취소 성공", null);
    }

    // 추후 활성화 예정
    // @GetMapping("/my-rentals")
    // public ApiResponse<List<Equipment>> getMyRentals(@RequestParam Long userId) {
    //     return ApiResponse.success("내 대여 목록 조회 성공", equipmentService.getMyRentals(userId));
    // }
}
