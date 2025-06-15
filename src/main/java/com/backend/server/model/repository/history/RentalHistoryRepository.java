package com.backend.server.model.repository.history;

import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.equipment.Equipment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RentalHistoryRepository extends
        JpaRepository<RentalHistory, Long>, JpaSpecificationExecutor<RentalHistory> {

    // 대여 내역 목록 조회
    @EntityGraph(attributePaths = {"classroom", "renter", "equipment",
            "equipment.equipmentModel", "equipment.equipmentCategory", "brokenHistory", "repairHistory"})
    Page<RentalHistory> findAll(Specification<RentalHistory> spec, Pageable pageable);

    // 대여 내역 최근 한건 조회
    Optional<RentalHistory> findFirstByEquipmentAndRenterOrderByCreatedAtDesc(Equipment equipment, User renter);
    Optional<RentalHistory> findFirstByClassroomAndRenterOrderByCreatedAtDesc(Classroom classroom, User renter);
}