package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
}