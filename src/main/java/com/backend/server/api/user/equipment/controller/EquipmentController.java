package com.backend.server.api.user.equipment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.user.equipment.dto.EquipmentListRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.equipment.service.EquipmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/equipments")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    //장비목록조회, 근데 자신의 제한 학년 품목은 안보임
    @GetMapping
    public ApiResponse<EquipmentListResponse> getEquipments(@ModelAttribute EquipmentListRequest request) {
        return ApiResponse.success("장비 목록 조회 성공", equipmentService.getEquipments(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<EquipmentResponse> getEquipment(@PathVariable Long id) {
        return ApiResponse.success("장비 상세 조회 성공", equipmentService.getEquipment(id));
    }
    
}
