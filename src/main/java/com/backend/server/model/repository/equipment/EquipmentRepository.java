package com.backend.server.model.repository.equipment;

import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.enums.Status;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import org.springframework.data.repository.query.Param;

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
            + "e.startRentTime = :start, "
            + "e.endRentTime = :end, "
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
            + "e.startRentTime = null, "
            + "e.endRentTime = null, "
            + "e.semesterSchedule = null "
            + "WHERE e IN :equipments")
    void cancelRent(List<Equipment> equipments, Status status);

    //관리자 장비 상태 다중 업데이트
    @Modifying
    @Query("UPDATE Equipment e SET e.status = :status WHERE e.id IN :ids")
    void bulkUpdateStatus(@Param("status") String status, @Param("ids") List<Long> ids);


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


    List<Equipment> findBySerialNumberStartingWith(String serialNumber);

    int countByRenterAndEquipmentCategoryAndStatusIn(User renter, EquipmentCategory category, List<Status> statusList);

}