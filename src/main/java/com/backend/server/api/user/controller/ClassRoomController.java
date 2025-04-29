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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classrooms")
public class ClassRoomController {
    private final ClassRoomService classRoomService;

    //강의실 목록 조회
    @GetMapping
    public ApiResponse<ClassRoomListResponse> getClassRooms(CommonClassRoomListRequest request) {
        return ApiResponse.success("success", classRoomService.getClassRooms(request));
    }

    //강의실 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<ClassRoomResponse> getClassRoom(@PathVariable Long id) {
        return ApiResponse.success("success", classRoomService.getClassRoom(id));
    }

    //강의실 즐겨찾기 추가
    @PostMapping("/{id}/favorite")
    public ApiResponse<Void> addFavorite(@PathVariable Long id) {
        classRoomService.addFavorite(id);
        return ApiResponse.success("success", null);
    }

    //강의실 즐겨찾기 해제
    @PostMapping("/{id}/unfavorite")
    public ApiResponse<Void> removeFavorite(@PathVariable Long id) {
        classRoomService.removeFavorite(id);
        return ApiResponse.success("success", null);
    }

    //강의실 대여 신청
    @PostMapping("/rent-request")
    public ApiResponse<ClassRoomRentalResponse> createRentRequest(@RequestBody ClassRoomRentalRequest request) {
        return ApiResponse.success("success", classRoomService.createRentRequest(request));
    }

    //다중 강의실 대여 신청
    @PostMapping("/rent-requests")
    public ApiResponse<ClassRoomRentalListResponse> createRentRequests(@RequestBody ClassRoomRentalListRequest request) {
        return ApiResponse.success("success", classRoomService.createRentRequests(request));
    }

    //강의실 반납 신청
    @PostMapping("/return-request")
    public ApiResponse<ClassRoomRentalResponse> createReturnRequest(@RequestBody ClassRoomRentalRequest request) {
        return ApiResponse.success("success", classRoomService.createReturnRequest(request));
    }

    //다중 강의실 반납 신청
    @PostMapping("/return-requests")
    public ApiResponse<ClassRoomRentalListResponse> createReturnRequests(@RequestBody ClassRoomRentalListRequest request) {
        return ApiResponse.success("success", classRoomService.createReturnRequests(request));
    }

    //대여/반납 요청 취소
    @PostMapping("/cancel-request/{requestId}")
    public ApiResponse<Void> cancelRentalRequest(@PathVariable Long requestId) {
        classRoomService.cancelRentalRequest(requestId);
        return ApiResponse.success("success", null);
    }

    //즐겨찾기 목록 조회
    @GetMapping("/favorites")
    public ApiResponse<FavoriteListResponse> getFavorites(CommonClassRoomListRequest request) {
        return ApiResponse.success("success", classRoomService.getFavoriteList(request));
    }
}
