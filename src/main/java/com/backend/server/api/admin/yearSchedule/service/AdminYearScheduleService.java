package com.backend.server.api.admin.yearSchedule.service;

import com.backend.server.api.admin.classroom.dto.AdminClassroomResponse;
import com.backend.server.api.admin.yearSchedule.dto.AdminYearScheduleRequest;
import com.backend.server.api.admin.yearSchedule.dto.AdminYearScheduleResponse;
import com.backend.server.api.admin.yearSchedule.dto.AdminYearScheduleSearchRequest;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.YearSchedule;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import com.backend.server.model.repository.classroom.YearScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminYearScheduleService {

    private final YearScheduleRepository yearScheduleRepository;
    private final ClassroomRepository classroomRepository;

    @Transactional(readOnly = true)
    public List<AdminYearScheduleResponse> getYearSchedules(AdminYearScheduleSearchRequest request) {
        LocalDate startDate = LocalDate.of(request.getYear(), request.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return yearScheduleRepository.findWithClassroomBetweenDates(startDate, endDate)
                .stream()
                .map((entity) -> {
                    if (entity.getClassroom() == null)
                        return new AdminYearScheduleResponse(entity, null);
                    AdminClassroomResponse classroom = new AdminClassroomResponse(
                            entity.getClassroom(),
                            entity.getClassroom().getManager());
                    return new AdminYearScheduleResponse(entity, classroom);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createYearSchedule(AdminYearScheduleRequest request) {

        if (!request.getIsHoliday()) {
            if (request.getClassroomId() == null)
                throw new IllegalArgumentException("강의실id 가 유효하지 않습니다.");

            classroomRepository
                    .findById(request.getClassroomId())
                    .orElseThrow(()->new IllegalArgumentException("강의실id 가 유효하지 않습니다."));

            if (request.getStartAt().isAfter(request.getEndAt()))
                throw new IllegalArgumentException("일정 종료 < 일정 시작 입니다.");
        }

        return yearScheduleRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public Long updateYearSchedule(Long id, AdminYearScheduleRequest request) {
        YearSchedule yearSchedule = yearScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 일정 ID입니다."));

        if (!yearSchedule.getDate().equals(request.getDate()))
            throw new IllegalArgumentException("DB에 저장된 날짜와 다릅니다.");

        if (!request.getIsHoliday()) {
            if (request.getClassroomId() == null)
                throw new IllegalArgumentException("강의실id 가 유효하지 않습니다.");

            classroomRepository
                    .findById(request.getClassroomId())
                    .orElseThrow(()->new IllegalArgumentException("강의실id 가 유효하지 않습니다."));

            if (request.getStartAt().isAfter(request.getEndAt()))
                throw new IllegalArgumentException("일정 종료 < 일정 시작 입니다.");

            yearSchedule = yearSchedule.toBuilder()
                    .isHoliday(request.getIsHoliday())
                    .classroom(Classroom.builder().id(request.getClassroomId()).build())
                    .description(request.getDescription())
                    .color(request.getColor())
                    .startAt(request.getStartAt())
                    .endAt(request.getEndAt())
                    .build();
        } else {
            yearSchedule = yearSchedule.toBuilder()
                    .isHoliday(request.getIsHoliday())
                    .classroom(null)
                    .description(request.getDescription())
                    .color(request.getColor())
                    .startAt(null)
                    .endAt(null)
                    .build();
        }

        yearScheduleRepository.save(yearSchedule);
        return yearSchedule.getId();
    }

    @Transactional
    public void deleteYearSchedule(Long id) {
        yearScheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("유효하지 않은 일정 ID입니다."));
        yearScheduleRepository.deleteById(id);
    }
}