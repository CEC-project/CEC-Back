package com.backend.server.model.repository;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    List<Equipment> findByCategory(String category);
    List<Equipment> findByRenterId(Integer renterId);
    List<Equipment> findByManagerName(String managerName);
    List<Equipment> findByRentalStatus(RentalStatus rentalStatus);
} 