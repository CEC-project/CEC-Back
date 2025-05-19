package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.SemesterSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterScheduleRepository extends JpaRepository<SemesterSchedule, Integer> {
}