package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.YearSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YearScheduleRepository extends JpaRepository<YearSchedule, Long> {
}