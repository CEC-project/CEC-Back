package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}