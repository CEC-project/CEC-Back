package com.backend.server.api.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-models")
@RequiredArgsConstructor
public class EquipmentModelController {
    private final EquipmentModelService equipmentModelService;

    @GetMapping
    public ApiResponse<EquipmentModelListResponse> getAllModels() {


        
        return ApiResponse.success("장비 모델 전체 조회 성공", equipmentModelService.getAllModels());
    }
    
}
