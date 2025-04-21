package com.backend.server.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.model.entity.EquipmentRental;

public interface EquipmentRentalRepository extends JpaRepository<EquipmentRental, Long> {
    
}
