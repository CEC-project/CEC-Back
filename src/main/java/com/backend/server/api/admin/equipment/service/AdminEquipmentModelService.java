package com.backend.server.api.admin.equipment.service;

import org.springframework.stereotype.Service;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelIdResponse;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.repository.EquipmentModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;

    //장비 모델 생성
    public AdminEquipmentModelIdResponse createModel(AdminEquipmentModelCreateRequest request) {
        EquipmentModel savedEquipmentModel = equipmentModelRepository.save(request.toEntity());
        return new AdminEquipmentModelIdResponse(savedEquipmentModel.getId());
    }

    //장비 모델 업데이트
    public AdminEquipmentModelIdResponse updateModel(Long id, AdminEquipmentModelCreateRequest request) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 장비 모델이 존재하지 않습니다. id=" + id));

        equipmentModel = equipmentModel.toBuilder()
            .name(request.getName())
            .available(request.isAvailable())
            .categoryId(request.getCategoryId())
            .englishCode(request.getEnglishCode())
            .build();

        EquipmentModel savedEquipmentModel = equipmentModelRepository.save(equipmentModel);
        return new AdminEquipmentModelIdResponse(savedEquipmentModel.getId());
    }

    //장비 모델 삭제
    public AdminEquipmentModelIdResponse deleteModel(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 장비 모델이 존재하지 않습니다. id=" + id));
        equipmentModelRepository.delete(equipmentModel);
        return new AdminEquipmentModelIdResponse(equipmentModel.getId());
    }
}
