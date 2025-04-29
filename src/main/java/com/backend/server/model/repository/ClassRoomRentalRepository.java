package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.entity.enums.RentalStatus;

@Repository
public interface ClassRoomRentalRepository extends JpaRepository<ClassRoomRental, Long>, JpaSpecificationExecutor<ClassRoomRental> {
    boolean existsByClassRoomIdAndStatus(Long classRoomId, RentalStatus status);
    ClassRoomRental findFirstByClassRoomIdOrderByCreatedAtDesc(Long classRoomId);
} 