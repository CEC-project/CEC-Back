package com.backend.server.model.repository.equipment;

import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentBrokenHistory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.entity.equipment.EquipmentRepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EquipmentRepairHistoryRepository extends JpaRepository<EquipmentRepairHistory, Long>, JpaSpecificationExecutor<EquipmentRepairHistory> {

}
