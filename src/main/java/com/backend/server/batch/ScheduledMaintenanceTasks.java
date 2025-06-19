package com.backend.server.batch;

import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import com.backend.server.model.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class ScheduledMaintenanceTasks {
    private final EquipmentRepository equipmentRepository;
    private final ClassroomRepository classroomRepository;
    private final SemesterRepository semesterRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void midnightScheduler() {
        runSoftDeleteCleanup();
        runExpiredRentalCleanup();
    }

    @Transactional
    public void runExpiredRentalCleanup() {
        equipmentRepository.updateStatusByStartRentTimeBefore(Status.RENTAL_PENDING, Status.AVAILABLE, LocalDateTime.now());
        classroomRepository.updateClassroomStatusFromTo(Status.RENTAL_PENDING, Status.AVAILABLE);

        //끝난 학기중 가장 최신인 학기 가져오기
        Semester semester = semesterRepository.findTopByEndDateBeforeOrderByEndDateDesc(LocalDate.now());
        //끝난 학기 없으면 NPE, 그래서 NPE체크 && 지난학기 맞는지 체크
        if (semester != null && LocalDate.now().isAfter(semester.getEndDate())) {
            equipmentRepository.updateEquipmentStatusToAvailableBySemester(
                    semester.getId(), Status.IN_USE.name(), Status.AVAILABLE.name());
            classroomRepository.updateClassroomStatusToAvailableBySemester(
                    semester.getId(), Status.IN_USE, Status.AVAILABLE);
        }
    }

    @Transactional
    public void runSoftDeleteCleanup() {
        LocalDateTime cutoff = LocalDateTime.now().minusYears(1);
        userRepository.deleteByDeletedAtBefore(cutoff);
        equipmentRepository.deleteByDeletedAtBefore(cutoff);
    }
}





