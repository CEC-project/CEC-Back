package com.backend.server.model.repository;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    //Long countByEquipmentModel_Id(Long id);
    // List<Equipment> findByCategoryId(Long categoryId);
    // List<Equipment> findByRenterId(Integer renterId);
    // List<Equipment> findByManagerName(String managerName);
    // List<Equipment> findByRentalStatus(Status rentalStatus);
    // Integer countByRentalStatus(Status rentalStatus);
    // List<Equipment> findByRentalStatusAndName(Status status, String name);
    Long countByModelId(Long modelId);
    Optional<User> findByRenterId(Long renterId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Equipment e WHERE e.id = :id")
    Optional<Equipment> findByIdForUpdate(@Param("id") Long id);
    
    // 상태별 장비 조회
    List<Equipment> findByStatus(Status status);
    
    // 대여자 ID로 장비 목록 조회 (내림차순)
    List<Equipment> findByRenterIdOrderByRequestedAtDesc(Long renterId);
    
    // 상태별 장비 개수 조회
    long countByStatus(Status status);
} 