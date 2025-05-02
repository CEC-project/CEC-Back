package com.backend.server.api.admin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.dto.equipment.AdminEquipmentIdResponse;
import com.backend.server.api.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin/equipments")
public class AdminEquipmentController {
    
    private final AdminEquipmentService adminEquipmentService;
    //장비 등록
    @PostMapping
    public ApiResponse<AdminEquipmentIdResponse> createEquipment(@RequestBody PastAdminEquipmentCreateRequest request) {
        return ApiResponse.success(adminEquipmentService.createEquipment(request));
    }
}
