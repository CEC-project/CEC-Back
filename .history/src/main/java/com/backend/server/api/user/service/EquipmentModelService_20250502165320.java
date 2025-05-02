package com.backend.server.api.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.user.dto.equipment.equipment.EquipmentModelListRequest;
import com.backend.server.api.user.dto.equipment.equipment.EquipmentModelListResponse;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.repository.EquipmentModelRepository;
import com.backend.server.model.repository.EquipmentModelSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;

    public EquipmentModelListResponse getAllModels(EquipmentModelListRequest request) {
        Pageable pageable = EquipmentModelSpecification.getPageable(request);
        Specification<EquipmentModel> spec = EquipmentModelSpecification.filterEquipments(request);
        Page<EquipmentModel> page = equipmentModelRepository.findAll(spec, pageable);
        
        return new EquipmentModelListResponse(page);
    }
}
