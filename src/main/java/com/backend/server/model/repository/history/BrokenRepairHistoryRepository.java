package com.backend.server.model.repository.history;

import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.equipment.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BrokenRepairHistoryRepository extends JpaRepository<BrokenRepairHistory, Long>, JpaSpecificationExecutor<BrokenRepairHistory> {
    Optional<BrokenRepairHistory> findTopByEquipmentAndHistoryTypeOrderByCreatedAtDesc(
            Equipment equipment,
            BrokenRepairHistory.HistoryType historyType
    );

    Optional<BrokenRepairHistory> findTopByClassroomAndHistoryTypeOrderByCreatedAtDesc(
            Classroom classroom,
            BrokenRepairHistory.HistoryType historyType
    );
}