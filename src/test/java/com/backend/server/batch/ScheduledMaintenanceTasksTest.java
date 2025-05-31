package com.backend.server.batch;

import com.backend.server.config.AbstractPostgresContainerTest;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.classroom.SemesterScheduleRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ScheduledMaintenanceTasksTest extends AbstractPostgresContainerTest {

    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    ClassroomRepository classroomRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    SemesterScheduleRepository semesterScheduleRepository;
    @Autowired
    EntityManager em;
    private ScheduledMaintenanceTasks scheduledMaintenanceTasks;
    private Semester semester;
    private Classroom classroom;
    private Equipment eq1, eq2;
    private Equipment rentalPendingEquipment;
    private Classroom rentalPendingClassroom;


    @BeforeEach
    void setUp() {
        semesterRepository.deleteAll();
        classroomRepository.deleteAll();
        equipmentRepository.deleteAll();

        //배치 스케줄러 인스턴스 생성하기
        scheduledMaintenanceTasks = new ScheduledMaintenanceTasks(
                equipmentRepository,
                classroomRepository,
                semesterRepository
        );

        // 테스트용 학기
        semester = semesterRepository.save(Semester.builder()
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 5, 20))
                .year(2025)
                .name("테스트학기")
                .build());

        // 테스트용 강의실
        classroom = classroomRepository.save(Classroom.builder()
                .name("101호")
                .status(Status.IN_USE)
                .build());

        // RENTAL_PENDING 상태의 강의실
        rentalPendingClassroom = classroomRepository.save(Classroom.builder()
                .name("102호")
                .status(Status.RENTAL_PENDING)
                .build());

        // IN_USE 상태의 장비 (학기 스케줄과 연결됨)
        eq1 = equipmentRepository.save(Equipment.builder()
                .status(Status.IN_USE)
                .restrictionGrade("3")
                .brokenCount(0L)
                .description("테스트 장비 1")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .build());

        // 이미 AVAILABLE 상태인 장비 (변경되지 않아야 함)
        eq2 = equipmentRepository.save(Equipment.builder()
                .status(Status.AVAILABLE)
                .restrictionGrade("4")
                .brokenCount(0L)
                .description("테스트 장비 2")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .build());

        // RENTAL_PENDING 상태의 장비 (startRentDate가 과거)
        rentalPendingEquipment = equipmentRepository.save(Equipment.builder()
                .status(Status.RENTAL_PENDING)
                .restrictionGrade("2")
                .brokenCount(0L)
                .description("대여 대기 장비")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .startRentDate(LocalDateTime.now().minusDays(1)) // 어제로 설정
                .build());

        // 학기 스케줄 생성
        SemesterSchedule schedule = semesterScheduleRepository.save(SemesterSchedule.builder()
                .name("스프링부트 수업")
                .year(2025)
                .day(1)
                .color("#FFAA00")
                .startAt(LocalTime.of(9, 0))
                .endAt(LocalTime.of(11, 0))
                .semester(semester)
                .classroom(classroom)
                .equipments(List.of(eq1, eq2))
                .build());

        // 장비에 스케줄 연결
        eq1 = eq1.toBuilder()
                .semesterSchedule(schedule)
                .build();
        equipmentRepository.save(eq1);

        eq2 = eq2.toBuilder()
                .semesterSchedule(schedule)
                .build();
        equipmentRepository.save(eq2);

        em.flush();
        em.clear();



    }
    @Test
    @DisplayName("만료된 대여 정리 작업이 정상적으로 동작한다")
    void testRunExpiredRentalCleanup() {
        // given
        //없어도됌


        // when
        scheduledMaintenanceTasks.runExpiredRentalCleanup();

        // then
        em.flush();
        em.clear();

        // RENTAL_PENDING 장비가 AVAILABLE로 변경되었는지 확인
        Equipment updatedRentalPendingEquipment = equipmentRepository.findById(rentalPendingEquipment.getId()).orElse(null);
        assertThat(updatedRentalPendingEquipment.getStatus()).isEqualTo(Status.AVAILABLE);

        // RENTAL_PENDING 강의실이 AVAILABLE로 변경되었는지 확인
        Classroom updatedRentalPendingClassroom = classroomRepository.findById(rentalPendingClassroom.getId()).orElse(null);
        assertThat(updatedRentalPendingClassroom.getStatus()).isEqualTo(Status.AVAILABLE);

        // 학기가 종료되어 IN_USE 장비가 AVAILABLE로 변경되었는지 확인
        Equipment updatedEq1 = equipmentRepository.findById(eq1.getId()).orElse(null);
        assertThat(updatedEq1.getStatus()).isEqualTo(Status.AVAILABLE);

        // 학기가 종료되어 IN_USE 강의실이 AVAILABLE로 변경되었는지 확인
        Classroom updatedClassroom = classroomRepository.findById(classroom.getId()).orElse(null);
        assertThat(updatedClassroom.getStatus()).isEqualTo(Status.AVAILABLE);

        // 이미 AVAILABLE 상태인 장비는 변경되지 않았는지 확인
        Equipment updatedEq2 = equipmentRepository.findById(eq2.getId()).orElse(null);
        assertThat(updatedEq2.getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    @DisplayName("종료된 학기가 없을 때도 RENTAL_PENDING 정리는 동작한다")
    void testRunExpiredRentalCleanupWithoutExpiredSemester() {
        // given
        // 기존 학기의 종료일을 미래로 업데이트 (새로운 엔티티 생성하지 않음)
        semester = semester.toBuilder()
                .endDate(LocalDate.now().plusDays(30))
                .build();
        semesterRepository.save(semester);

        // when
        scheduledMaintenanceTasks.runExpiredRentalCleanup();

        // then
        em.flush();
        em.clear();

        // RENTAL_PENDING 장비만 AVAILABLE로 변경되었는지 확인
        Equipment updatedRentalPendingEquipment = equipmentRepository.findById(rentalPendingEquipment.getId()).orElse(null);
        assertThat(updatedRentalPendingEquipment.getStatus()).isEqualTo(Status.AVAILABLE);

        // RENTAL_PENDING 강의실만 AVAILABLE로 변경되었는지 확인
        Classroom updatedRentalPendingClassroom = classroomRepository.findById(rentalPendingClassroom.getId()).orElse(null);
        assertThat(updatedRentalPendingClassroom.getStatus()).isEqualTo(Status.AVAILABLE);

        // IN_USE 장비는 변경되지 않았는지 확인 (학기가 아직 종료되지 않음)
        Equipment updatedEq1 = equipmentRepository.findById(eq1.getId()).orElse(null);
        assertThat(updatedEq1.getStatus()).isEqualTo(Status.IN_USE);

        // IN_USE 강의실은 변경되지 않았는지 확인
        Classroom updatedClassroom = classroomRepository.findById(classroom.getId()).orElse(null);
        assertThat(updatedClassroom.getStatus()).isEqualTo(Status.IN_USE);
    }

    @Test
    @DisplayName("자정에 스케줄러가 정상적으로 동작한다")
    void testMidnightScheduler() {
        // when
        scheduledMaintenanceTasks.midnightScheduler();

        // then
        em.flush();
        em.clear();

        // 기본적으로 runExpiredRentalCleanup과 동일한 동작을 해야 함
        Equipment updatedRentalPendingEquipment = equipmentRepository.findById(rentalPendingEquipment.getId()).orElse(null);
        assertThat(updatedRentalPendingEquipment.getStatus()).isEqualTo(Status.AVAILABLE);

        Classroom updatedRentalPendingClassroom = classroomRepository.findById(rentalPendingClassroom.getId()).orElse(null);
        assertThat(updatedRentalPendingClassroom.getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    @DisplayName("startRentDate가 미래인 RENTAL_PENDING 장비는 변경되지 않는다")
    void testRentalPendingEquipmentWithFutureStartDate() {
        // given
        Equipment futureRentalEquipment = equipmentRepository.save(Equipment.builder()
                .status(Status.RENTAL_PENDING)
                .restrictionGrade("1")
                .brokenCount(0L)
                .description("미래 대여 장비")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .startRentDate(LocalDateTime.now().plusDays(1)) // 내일로 설정
                .build());



        // when
        scheduledMaintenanceTasks.runExpiredRentalCleanup();

        // then
        em.flush();
        em.clear();

        Equipment updatedFutureRentalEquipment = equipmentRepository.findById(futureRentalEquipment.getId()).orElse(null);
        assert updatedFutureRentalEquipment != null;
        assertThat(updatedFutureRentalEquipment.getStatus()).isEqualTo(Status.RENTAL_PENDING);
    }


}
