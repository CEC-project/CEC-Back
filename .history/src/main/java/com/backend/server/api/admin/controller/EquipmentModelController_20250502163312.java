package com.backend.server.api.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.admin.dto.equipment.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.dto.equipment.model.AdminEquipmentModelIdResponse;
import com.backend.server.api.admin.service.AdminEquipmentModelService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.model.entity.EquipmentModel;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-models")
@RequiredArgsConstructor
public class EquipmentModelController {

    private final AdminEquipmentModelService adminEquipmentModelService;
    private final EquipmentModelService equipmentModelService;

    @PostMapping
    public ApiResponse<AdminEquipmentModelIdResponse> createModel(@Valid @RequestBody AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 생성 성공", adminEquipmentModelService.createModel(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<AdminEquipmentModelIdResponse> updateModel(@PathVariable Long id, @Valid @RequestBody AdminEquipmentModelCreateRequest request) {
        return ApiResponse.success("장비 모델 수정 성공", adminEquipmentModelService.updateModel(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<AdminEquipmentModelIdResponse> deleteModel(@PathVariable Long id) {
        return ApiResponse.success("장비 모델 삭제 성공", adminEquipmentModelService.deleteModel(id));
    }

    //====================================================유저에서 가져옴====================================================
    @GetMapping
    public ApiResponse<List<EquipmentModel>> getAllModels() {
        return ApiResponse.success("장비 모델 전체 조회 성공", equipmentModelService.getAllModels());
    }


    @GetMapping("/{id}")
    public EquipmentModel getModelById(@PathVariable Long id) {
        return equipmentModelService.getModelById(id);
    }
}