package com.backend.server.model.repository.equipment;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.enums.Status;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
    Long countByEquipmentModel_Id(Long modelId);
    Optional<User> findByRenterId(Long renterId);

    Long countBySerialNumberStartingWith(String prefix);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Equipment e WHERE e.id = :id")
    Optional<Equipment> findByIdForUpdate(@Param("id") Long id);
    
    // 상태별 장비 조회
    List<Equipment> findByStatus(Status status);
    
    // 대여자 ID로 장비 목록 조회 (내림차순)
    // List<Equipment> findByRenterIdOrderByRequestedAtDesc(Long renterId);
    
    // 상태별 장비 개수 조회
    long countByStatus(Status status);

    // 수업 생성시 장비 대여 가능 여부 확인
    @Query("SELECT COUNT(e.id) = :size "
            + "FROM Equipment e "
            + "WHERE e.id IN :ids AND e.status = :status")
    boolean isAvailableAllByIdIn(@Param("ids") List<Long> ids, @Param("size") long size,
            @Param("status") Status status);

    // 수업 수정시 장비 대여 가능 여부 확인
    @Query("SELECT COUNT(e.id) = :size "
            + "FROM Equipment e "
            + "LEFT JOIN e.semesterSchedule ss "
            + "WHERE e.id IN :ids AND ("
            + "    e.status = :status OR "
            + "    ss = :schedule"
            + ")")
    boolean isAvailableAllByIdIn(@Param("ids") List<Long> ids, @Param("size") long size,
            @Param("schedule") SemesterSchedule schedule, @Param("status") Status status);

    // 수업에 필요한 장비 대여
    @Modifying
    @Query("UPDATE Equipment e SET "
            + "e.status = :status, "
            + "e.startRentDate = :start, "
            + "e.endRentDate = :end, "
            + "e.semesterSchedule = :schedule "
            + "WHERE e.id IN :ids")
    void rentByIds(
            @Param("ids") List<Long> ids,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("schedule") SemesterSchedule schedule,
            @Param("status") Status status
    );

    // 수업에 필요한 장비 대여 취소
    @Modifying
    @Query("UPDATE Equipment e SET "
            + "e.status = :status, "
            + "e.startRentDate = null, "
            + "e.endRentDate = null, "
            + "e.semesterSchedule = null "
            + "WHERE e IN :equipments")
    void cancelRent(List<Equipment> equipments, Status status);
}