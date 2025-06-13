package com.backend.server.model.repository.equipment;

import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.enums.Status;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
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
    Long countByEquipmentModel_Id(Long modelId);

    Long countBySerialNumberStartingWith(String prefix);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Equipment e WHERE e.id = :id")
    Optional<Equipment> findByIdForUpdate(@Param("id") Long id);

    // 수업 생성시 장비 대여 가능 여부 확인
    @Query("SELECT COUNT(e.id) = :size "
            + "FROM Equipment e "
            + "WHERE e.id IN :ids AND e.status = :availableStatus")
    boolean isAvailableAllByScheduleAndIds(@Param("ids") List<Long> ids, @Param("size") long size,
            @Param("availableStatus") Status availableStatus);

    // 수업 수정시 장비 대여 가능 여부 확인
    @Query("SELECT COUNT(e.id) = :size "
            + "FROM Equipment e "
            + "LEFT JOIN e.semesterSchedule ss "
            + "WHERE e.id IN :ids AND ("
            + "    e.status = :availableStatus OR "
            + "    ss = :schedule"
            + ")")
    boolean isAvailableAllByScheduleAndIds(@Param("ids") List<Long> ids, @Param("size") long size,
            @Param("schedule") SemesterSchedule schedule, @Param("availableStatus") Status availableStatus);

    // 수업에 필요한 장비 대여
    @Modifying
    @Query("UPDATE Equipment e SET "
            + "e.status = :inUseStatus, "
            + "e.startRentTime = :start, "
            + "e.endRentTime = :end, "
            + "e.semesterSchedule = :schedule "
            + "WHERE e.id IN :ids")
    void rentByScheduleAndIds(
            @Param("ids") List<Long> ids,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("schedule") SemesterSchedule schedule,
            @Param("inUseStatus") Status inUseStatus
    );

    // 수업 수정시 대여 취소시킬 장비 목록
    // 조건1. 현재 수업에서 이미 대여중이던 장비인가?
    // 조건2. 현재 대여할 장비 목록에 없는가?
    @Query("""
    SELECT e
    FROM Equipment e
    WHERE e.semesterSchedule = :schedule
    AND e.id IN :equipments""")
    List<Equipment> findByScheduleAndNotInIds(
            @Param("schedule") SemesterSchedule schedule,
            @Param("equipments") List<Long> equipments);

    // 수업에 필요한 장비 대여 취소
    @Modifying
    @Query("UPDATE Equipment e SET "
            + "e.status = :availableStatus, "
            + "e.startRentTime = null, "
            + "e.endRentTime = null, "
            + "e.semesterSchedule = null "
            + "WHERE e IN :equipments")
    void cancelRentByIds(List<Equipment> equipments, Status availableStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Equipment e SET e.status = :newStatus WHERE e.status = :targetStatus AND e.startRentTime < :now")
    void updateStatusByStartRentTimeBefore(@Param("targetStatus") Status targetStatus,
                                           @Param("newStatus") Status newStatus,
                                           @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    //특정 학기(semesterId)에 등록된 강의 시간표(SemesterSchedule)에 포함된 장비들 중,
    //현재 상태가 IN_USE인 장비만 골라서
    //AVAILABLE 상태로 일괄 변경합니다.
    @Query(value = """
            UPDATE equipment e
            SET status = :availableStatus
            FROM semester_schedule ss
            WHERE e.renter_semester_schdule_id = ss.id
            AND ss.semester_id = :semesterId
            AND e.status = :inUseStatus""",
            nativeQuery = true)
    void updateEquipmentStatusToAvailableBySemester(
            @Param("semesterId") Long semesterId,
            @Param("inUseStatus") String inUseStatus,
            @Param("availableStatus") String availableStatus
    );

    @Query(value = "SELECT e.serialNumber FROM Equipment e " + // JPQL 사용 (엔티티명 사용)
            "WHERE e.serialNumber LIKE :serialPrefix || '%' " + // JPQL의 CONCATENATION은 DB에 따라 다를 수 있으나, 일반적으로 || 사용
            "ORDER BY LENGTH(e.serialNumber) DESC, e.serialNumber DESC")
    Optional<String> findTopBySerialNumberStartingWithOrderBySerialNumberDesc(@Param("serialPrefix") String serialPrefix);
    Optional<Equipment> findBySerialNumber(String serialNumber);

}