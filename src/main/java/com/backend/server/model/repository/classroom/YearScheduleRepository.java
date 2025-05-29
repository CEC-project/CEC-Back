package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.classroom.YearSchedule;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface YearScheduleRepository extends JpaRepository<YearSchedule, Long> {

    @EntityGraph(attributePaths = {"classroom"})
    @Query("SELECT y FROM YearSchedule y " +
            "WHERE y.date BETWEEN :startDate AND :endDate " +
            "ORDER BY y.date ASC")
    List<YearSchedule> findWithClassroomBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<YearSchedule> findByDateBetweenAndIsHolidayTrue(LocalDate monday, LocalDate friday);

    List<YearSchedule> findByDateBetweenAndClassroom(LocalDate monday, LocalDate friday, Classroom classroom);

    @EntityGraph(attributePaths = {"renter"})
    List<YearSchedule> findWithRenterByDate(LocalDate localDate);
}