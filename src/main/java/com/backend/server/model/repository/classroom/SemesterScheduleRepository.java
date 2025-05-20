package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.SemesterSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SemesterScheduleRepository extends JpaRepository<SemesterSchedule, Long> {

    @Query("SELECT ss "
            + "FROM SemesterSchedule ss "
            + "LEFT JOIN FETCH ss.professor "
            + "WHERE ss.semester.id = :semesterId "
            + "AND ss.classroom.id = :classroomId "
            + "ORDER BY ss.day asc, ss.startAt asc")
    List<SemesterSchedule> getSemesterScheduleBySemesterAndClassroom(Long semesterId, Long classroomId);
}