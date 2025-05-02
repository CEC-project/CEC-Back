package com.backend.server.api.admin.service;

import org.springframework.stereotype.Service;

import com.backend.server.api.admin.dto.equipment.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.dto.equipment.model.AdminEquipmentModelIdResponse;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.repository.EquipmentModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;

    public AdminEquipmentModelIdResponse createModel(AdminEquipmentModelCreateRequest request) {
        EquipmentModel savedEquipmentModel = equipmentModelRepository.save(request.toEntity());
        return new AdminEquipmentModelIdResponse(savedEquipmentModel.getId());
    }

    public AdminEquipmentModelIdResponse updateModel(Long id, AdminEquipmentModelCreateRequest request) {
}
