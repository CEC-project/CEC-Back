package com.backend.server.api.user.classroom.service;

import com.backend.server.api.user.classroom.dto.ClassroomRentalRequest;
import com.backend.server.api.user.classroom.dto.ClassroomResponse;
import com.backend.server.api.user.classroom.dto.ScheduleResponse;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.SemesterRepository;
import com.backend.server.model.repository.classroom.SemesterScheduleRepository;
import com.backend.server.model.repository.classroom.YearScheduleRepository;
import com.backend.server.util.CompareUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final YearScheduleRepository yearScheduleRepository;
    private final SemesterScheduleRepository semesterScheduleRepository;
    private final SemesterRepository semesterRepository;

    @Transactional
    public Long rental(Long renterId, Long classroomId, ClassroomRentalRequest request) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 id 입니다."));
        Classroom classroom = classroomRepository.findWithLockById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 강의실 id 입니다."));

        if (classroom.getStatus() != Status.AVAILABLE)
            throw new IllegalArgumentException("현재 대여 불가한 강의실 입니다.");

        LocalDateTime now = LocalDateTime.now();
        Integer day = now.getDayOfWeek().getValue();
        LocalTime startAt = request.getStartAt();
        LocalTime endAt = request.getEndAt();

        // 연간 일정과 겹치는지 체크
        List<YearSchedule> yearSchedules = yearScheduleRepository.findWithRenterByDate(now.toLocalDate());

        boolean isHoliday = yearSchedules.stream().anyMatch(YearSchedule::getIsHoliday);
        if (isHoliday)
            throw new IllegalArgumentException("휴일에는 강의실을 대여 할 수 없습니다.");

        boolean hasIntersectionWithSchedule = yearSchedules.stream().anyMatch(ys -> CompareUtils
                .hasIntersectionInclusive(ys.getStartAt(), ys.getEndAt(), startAt, endAt));
        if (hasIntersectionWithSchedule)
            throw new IllegalArgumentException("특강과 강의실 대여 시간이 겹칩니다.");

        // 수업과 겹치는지 체크
        List<Semester> semesters = semesterRepository.findSemesterContainingDate(now.toLocalDate());
        boolean hasIntersectionWithAnyLecture = semesterScheduleRepository
                .findByClassroomAndSemesterIn(classroom, semesters)
                .stream()
                .filter((ss) -> Objects.equals(ss.getDay(), day))
                .anyMatch((ss) -> CompareUtils.hasIntersectionExclusive(
                        ss.getStartAt(), ss.getEndAt(), startAt, endAt));
        if (hasIntersectionWithAnyLecture)
            throw new IllegalArgumentException("수업과 강의실 대여 시간이 겹칩니다.");

        classroom.toBuilder()
                .renter(renter)
                .startTime(startAt)
                .endTime(endAt)
                .status(Status.RENTAL_PENDING)
                .build();

        classroomRepository.save(classroom);
        return classroom.getId();
    }

    @Transactional
    public Long cancelRental(Long renterId, Long rentalId) {
        Classroom classroom = classroomRepository.findWithLockAndRenterById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 대여 id 입니다."));

        if (classroom.getRenter().getId().compareTo(renterId) != 0)
            throw new IllegalArgumentException("대여한 사용자와 로그인한 사용자가 다릅니다.");

        if (classroom.getStatus() != Status.RENTAL_PENDING)
            throw new IllegalArgumentException("대여 신청중이 아니면 대여 신청을 취소할수 없습니다.");

        classroom.toBuilder()
                .renter(null)
                .startTime(null)
                .endTime(null)
                .status(Status.AVAILABLE)
                .build();

        classroomRepository.save(classroom);
        return classroom.getId();
    }

    @Transactional(readOnly = true)
    public List<ClassroomResponse> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(ClassroomResponse::new)
                .toList();
    }

    /**@param date 조회할 주차의 아무 날짜*/
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(LocalDate date, Long classroomId) {
        LocalDate monday = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate friday = date.plusDays(5 - date.getDayOfWeek().getValue());

        Classroom classroom = classroomRepository.findWithRenterById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 강의실 id 입니다."));

        // 조회할 주차의 연간 일정 조회
        List<YearSchedule> yearSchedules = yearScheduleRepository.findByDateBetweenAndIsHolidayTrue(monday, friday);
        yearSchedules.addAll(yearScheduleRepository.findByDateBetweenAndClassroom(monday, friday, classroom));
        List<ScheduleResponse> result = yearSchedules.stream()
                .map(ScheduleResponse::new)
                .collect(Collectors.toList());

        // 조회할 주차의 수업 시간표 조회
        List<Semester> semesters = semesterRepository.findSemesterContainingAnyDate(monday, friday);
        List<SemesterSchedule> semesterSchedules = semesterScheduleRepository
                .findByClassroomAndSemesterIn(classroom, semesters)
                .stream()
                .filter((ss) -> !monday.plusDays(ss.getDay() - 1).isAfter(ss.getSemester().getEndDate()))
                .filter((ss) -> !monday.plusDays(ss.getDay() - 1).isBefore(ss.getSemester().getStartDate()))
                .toList();
        result.addAll(semesterSchedules
                .stream()
                .map((ss) -> new ScheduleResponse(ss, monday.plusDays(ss.getDay() - 1)))
                .toList());

        // 오늘 대여된 여부 조회
        if (classroom.getRenter() != null)
            result.add(new ScheduleResponse(classroom, classroom.getRenter()));

        // 요일, 시작 시간 순으로 정렬
        result.sort((a, b) -> {
            if (a.getDay().compareTo(b.getDay()) != 0) return a.getDay() - b.getDay();
            return a.getStartAt().compareTo(b.getStartAt());
        });

        return result;
    }
}
