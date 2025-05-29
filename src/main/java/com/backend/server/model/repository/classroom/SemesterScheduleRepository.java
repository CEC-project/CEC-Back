package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.Semester;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SemesterScheduleRepository extends JpaRepository<SemesterSchedule, Long> {

    @Query("SELECT DISTINCT ss "
            + "FROM SemesterSchedule ss "
            + "LEFT JOIN FETCH ss.professor "
            + "LEFT JOIN FETCH ss.equipments "
            + "WHERE ss.semester.id = :semesterId "
            + "AND ss.classroom.id = :classroomId "
            + "ORDER BY ss.day asc, ss.startAt asc")
    List<SemesterSchedule> getSemesterScheduleBySemesterAndClassroom(Long semesterId, Long classroomId);

    List<SemesterSchedule> findAllBySemester(Semester semester);
}