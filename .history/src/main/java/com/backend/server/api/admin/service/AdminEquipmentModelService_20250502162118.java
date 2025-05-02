package com.backend.server.api.admin.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;

    public EquipmentModel createModel(AdminEquipmentModelCreateRequest request) {
        return equipmentModelRepository.save(request.toEntity());
    }
}
