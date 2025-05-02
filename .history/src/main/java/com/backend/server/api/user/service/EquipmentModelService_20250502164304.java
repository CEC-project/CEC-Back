package com.backend.server.api.user.service;

import org.springframework.stereotype.Service;

import com.backend.server.model.repository.EquipmentModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentModelService {
    private final EquipmentModelRepository equipmentModelRepository;

    public EquipmentModelListResponse getAllModels() {
        return equipmentModelRepository.findAll();
    }
}
