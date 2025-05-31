package com.backend.server.model.repository.equipment;

import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentBrokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EquipmentBrokenHistoryRepository extends JpaRepository<EquipmentBrokenHistory, Long>, JpaSpecificationExecutor<EquipmentBrokenHistory> {
    Optional<EquipmentBrokenHistory> findTopByEquipmentOrderByCreatedAtDesc(Equipment equipment);

}
