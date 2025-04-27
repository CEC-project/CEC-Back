// package com.backend.server.api.admin.controller;
//
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.backend.server.api.admin.dto.category.AdminClassRoomCreateRequest;
// import com.backend.server.api.admin.dto.classroom.AdminClassRoomIdResponse;
// import com.backend.server.api.admin.service.AdminClassRoomService;
// import com.backend.server.api.common.dto.ApiResponse;
//
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/api/admin/classrooms")
// @RequiredArgsConstructor
// public class AdminClassRoomController {
//     private final AdminClassRoomService adminClassRoomService;
//
//
//     //강의실 등록
//     @PostMapping
//     public ApiResponse<AdminClassRoomIdResponse> createClassRoom(@Valid @RequestBody AdminClassRoomCreateRequest request) {
//         adminClassRoomService.createClassRoom(request);
//         return ApiResponse.success("success", null);
//     }
//
//     //강의실 업데이트
//     @PutMapping("/{id}")
//     public ApiResponse<AdminClassRoomIdResponse> updateClassRoom(@PathVariable Long id, @Valid @RequestBody AdminClassRoomCreateRequest request) {
//         adminClassRoomService.updateClassRoom(id, request);
//         return ApiResponse.success("success", null);
//     }
//
//     //강의실 삭제
//     @DeleteMapping("/{id}")
//     public ApiResponse<AdminClassRoomIdResponse> deleteClassRoom(@PathVariable Long id) {
//         adminClassRoomService.deleteClassRoom(id);
//         return ApiResponse.success("success", null);
//     }
// }
//
