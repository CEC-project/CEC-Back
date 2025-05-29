package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Lock;

public interface ClassroomRepository extends JpaRepository<Classroom, Long>, JpaSpecificationExecutor<Classroom> {
    @Modifying
    @Transactional
    @Query("UPDATE Classroom c SET c.status = :newStatus WHERE c.status = :currentStatus")
    void updateClassroomStatusFromTo(@Param("currentStatus") Status currentStatus,
                                     @Param("newStatus") Status newStatus);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Classroom c
    SET c.status = :availableStatus
    WHERE c.status = :inUseStatus
    AND c.id IN (
        SELECT ss.classroom.id
        FROM SemesterSchedule ss
        WHERE ss.semester.id = :semesterId
    )
""")
/**
 * 특정 학기(semesterId)에 등록된 시간표(SemesterSchedule)에 포함된 강의실 중,
 * 현재 상태가 IN_USE인 강의실만 골라 AVAILABLE 상태로 일괄 변경합니다.
 */
    void updateClassroomStatusToAvailableBySemester(
            @Param("semesterId") Long semesterId,
            @Param("inUseStatus") Status inUseStatus,
            @Param("availableStatus") Status availableStatus
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Classroom> findWithLockById(Long classroomId);

    @EntityGraph(attributePaths = {"renter"})
    Optional<Classroom> findWithRenterById(Long classroomId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"renter"})
    Optional<Classroom> findWithLockAndRenterById(Long classroomId);
}