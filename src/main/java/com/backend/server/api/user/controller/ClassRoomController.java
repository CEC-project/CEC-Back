package com.backend.server.api.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListRequest;
import com.backend.server.api.user.dto.classroom.ClassRoomListResponse;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalListRequest;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalListResponse;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalRequest;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalResponse;
import com.backend.server.api.user.dto.classroom.ClassRoomResponse;
import com.backend.server.api.user.dto.classroom.FavoriteListResponse;
import com.backend.server.api.user.service.ClassRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classrooms")
@Tag(name = "ClassRoom API", description = "강의실 관리 유저 API")
public class ClassRoomController {
    private final ClassRoomService classRoomService;

    //강의실 목록 조회
    @GetMapping
    @Operation(
    summary = "강의실 필터링 조회",
    description = "강의실의 상태, 대여 가능 여부, 검색어, 즐겨찾기 여부, 정렬 조건 등을 기반으로 강의실 목록을 필터링하여 조회합니다.")
    public ApiResponse<ClassRoomListResponse> getClassRooms(CommonClassRoomListRequest request) {
        return ApiResponse.success("success", classRoomService.getClassRooms(request));
    }

    //강의실 상세 조회
    @GetMapping("/{id}")   
    @Operation(
    summary = "강의실 상세 조회",
    description = "ID로 특정 강의실을 조회합니다.")
    public ApiResponse<ClassRoomResponse> getClassRoom(@PathVariable Long id) {
        return ApiResponse.success("success", classRoomService.getClassRoom(id));
    }

    //강의실 즐겨찾기 추가
    @PostMapping("/{id}/favorite")
    @Operation(
    summary = "강의실 즐겨찾기 추가",
    description = "ID로 특정 강의실을 즐겨찾기에 추가합니다.")
    public ApiResponse<Void> addFavorite(@PathVariable Long id) {
        classRoomService.addFavorite(id);
        return ApiResponse.success("success", null);
    }

    //강의실 즐겨찾기 해제
    @PostMapping("/{id}/unfavorite")
    @Operation(
    summary = "강의실 즐겨찾기 해제",
    description = "ID로 특정 강의실을 즐겨찾기에서 해제합니다.")
    public ApiResponse<Void> removeFavorite(@PathVariable Long id) {
        classRoomService.removeFavorite(id);
        return ApiResponse.success("success", null);
    }

    //강의실 대여 신청
    @PostMapping("/rent-request")
    @Operation(
    summary = "강의실 대여 신청",
    description = "강의실을 대여합니다.")
    public ApiResponse<ClassRoomRentalResponse> createRentRequest(@RequestBody ClassRoomRentalRequest request) {
        return ApiResponse.success("success", classRoomService.createRentRequest(request));
    }

    //다중 강의실 대여 신청
    @PostMapping("/rent-requests")
    @Operation(
    summary = "다중 강의실 대여 신청",
    description = "여러 강의실을 대여합니다.")
    public ApiResponse<ClassRoomRentalListResponse> createRentRequests(@RequestBody ClassRoomRentalListRequest request) {
        return ApiResponse.success("success", classRoomService.createRentRequests(request));
    }

    //강의실 반납 신청
    @PostMapping("/return-request")
    @Operation(
    summary = "강의실 반납 신청",
    description = "강의실을 반납합니다. 굳이 왜 만들었냐 하면 뭘 좋아하실지 몰라서 다 준비했어요요")
    public ApiResponse<ClassRoomRentalResponse> createReturnRequest(@RequestBody ClassRoomRentalRequest request) {
        return ApiResponse.success("success", classRoomService.createReturnRequest(request));
    }

    //다중 강의실 반납 신청
    @PostMapping("/return-requests")
    @Operation(
    summary = "다중 강의실 반납 신청",
    description = "여러 강의실을 반납합니다.")
    public ApiResponse<ClassRoomRentalListResponse> createReturnRequests(@RequestBody ClassRoomRentalListRequest request) {
        return ApiResponse.success("success", classRoomService.createReturnRequests(request));
    }

    //대여/반납 요청 취소
    @PostMapping("/cancel-request/{requestId}")
    @Operation(
    summary = "대여/반납 요청 취소",
    description = "대여/반납 요청을 취소합니다.")
    public ApiResponse<Void> cancelRentalRequest(@PathVariable Long requestId) {
        classRoomService.cancelRentalRequest(requestId);
        return ApiResponse.success("success", null);
    }

    //즐겨찾기 목록 조회
    @GetMapping("/favorites")
    @Operation(
    summary = "즐겨찾기 목록 조회",
    description = "즐겨찾기 목록을 조회합니다.")
    public ApiResponse<FavoriteListResponse> getFavorites(CommonClassRoomListRequest request) {
        return ApiResponse.success("success", classRoomService.getFavoriteList(request));
    }
}
