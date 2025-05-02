package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.model.entity.EquipmentModel;

public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long> {
    
}
