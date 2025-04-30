package com.backend.server.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.RentalStatus;

import java.util.List;

@Repository
public interface EquipmentRentalRepository extends JpaRepository<EquipmentRental, Long>, JpaSpecificationExecutor<EquipmentRental> {
    
    List<EquipmentRental> findByRentalStatus(RentalStatus rentalStatus);
    List<EquipmentRental> findByUserId(Long userId);
    List<EquipmentRental> findByEquipmentId(Long equipmentId);
}
