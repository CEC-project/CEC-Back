package com.backend.server.api.admin.equipment.service;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

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
    @Transactional
    public Long createModel(AdminEquipmentModelCreateRequest request) {
        checkExist(request);
        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));
        // modelGroupIndex 자동 생성 로직
        String modelEnglishCodePrefix = request.getEnglishCode().toUpperCase();
        String prefixForGrouping;
        if (modelEnglishCodePrefix.length() < 3) {
            // 영문코드가 3자리 이하인 경우 문자열 앞에 _ 를 붙여 3자리로 만듬
            prefixForGrouping = String.format("%3s", modelEnglishCodePrefix).replace(' ', '_');
        } else {
            prefixForGrouping = modelEnglishCodePrefix.substring(0, 3);
        }

        // 동일한 영문 코드 접두사를 가진 기존 모델들 중 가장 큰 modelGroupIndex 찾기
        Optional<EquipmentModel> latestModelInGroup = equipmentModelRepository.findTopByEnglishCodePrefixOrderByModelGroupIndexDesc(prefixForGrouping);

        int newModelGroupIndex = latestModelInGroup.map(EquipmentModel::getModelGroupIndex)
                .orElse(0) + 1; // 일치하는 모델이 없으면 0부터 시작하여 1을 더함

        // 모델 엔티티 생성 (modelGroupIndex 포함)
        EquipmentModel newModel = request.toEntity(equipmentCategory).toBuilder()
                .modelGroupIndex(newModelGroupIndex) // 자동 생성된 인덱스 할당
                .build();

        EquipmentModel savedEquipmentModel = equipmentModelRepository.save(newModel);
        return savedEquipmentModel.getId();
    }

    //장비 모델 업데이트
    @Transactional
    public Long updateModel(Long id, AdminEquipmentModelCreateRequest request) {
        checkExistForUpdate(id, request);
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 장비 모델이 존재하지 않습니다. id=" + id));

        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        equipmentModel = equipmentModel.toBuilder()
            .name(request.getName())
            .available(request.isAvailable())
            .category(equipmentCategory)
            .englishCode(request.getEnglishCode())
            .build();

        EquipmentModel savedEquipmentModel = equipmentModelRepository.save(equipmentModel);
        return savedEquipmentModel.getId();
    }

    //장비 모델 삭제
    @Transactional
    public Long deleteModel(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 장비 모델이 존재하지 않습니다. id=" + id));
        equipmentModel.softDelete();
        equipmentModelRepository.save(equipmentModel);
        return equipmentModel.getId();
    }
}
