package com.backend.server.api.user.equipment.service;

import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListResponse;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.model.repository.equipment.EquipmentModelSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;

    //장비 모델 검색 / 페이지네이션 조회
    public EquipmentModelListResponse getAllModels(EquipmentModelListRequest request) {
        Pageable pageable = request.toPageable();
        Specification<EquipmentModel> spec = EquipmentModelSpecification.filterEquipmentModels(request);
        Page<EquipmentModel> page = equipmentModelRepository.findAll(spec, pageable);
        
        return new EquipmentModelListResponse(page);
    }

    //장비 모델 상세 조회
    public EquipmentModelResponse getModel(Long id) {
        EquipmentModel model = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("장비 모델을 찾을 수 없습니다."));
        return new EquipmentModelResponse(model);
    }






}