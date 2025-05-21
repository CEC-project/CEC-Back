package com.backend.server.api.admin.equipment.service;

import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import org.springframework.stereotype.Service;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelIdResponse;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    public void checkExist(AdminEquipmentModelCreateRequest request){
        if (equipmentModelRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 모델 이름입니다.");
        }
        if (equipmentModelRepository.existsByEnglishCode(request.getEnglishCode())) {
            throw new IllegalArgumentException("이미 존재하는 영문 코드입니다.");
        }
        if (!equipmentCategoryRepository.existsById(request.getCategoryId())) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다. id=" + request.getCategoryId());
        }
    }

    //업데이트 중복검사
    private void checkExistForUpdate(Long id, AdminEquipmentModelCreateRequest request) {
        if (equipmentModelRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new IllegalArgumentException("이미 존재하는 모델 이름입니다.");
        }
        if (equipmentModelRepository.existsByEnglishCodeAndIdNot(request.getEnglishCode(), id)) {
            throw new IllegalArgumentException("이미 존재하는 영문 코드입니다.");
        }

        if (!equipmentCategoryRepository.existsById(request.getCategoryId())) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다. id=" + request.getCategoryId());
        }
    }

    //장비 모델 생성
    public AdminEquipmentModelIdResponse createModel(AdminEquipmentModelCreateRequest request) {
        checkExist(request);
        EquipmentModel savedEquipmentModel = equipmentModelRepository.save(request.toEntity());
        return new AdminEquipmentModelIdResponse(savedEquipmentModel.getId());
    }

    //장비 모델 업데이트
    public AdminEquipmentModelIdResponse updateModel(Long id, AdminEquipmentModelCreateRequest request) {
        checkExistForUpdate(id, request);
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
