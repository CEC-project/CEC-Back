package com.backend.server.model.repository.classroom;

import com.backend.server.model.entity.classroom.Semester;
import java.time.LocalDate;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findAllByOrderByStartDateAsc();

    @EntityGraph(attributePaths = {"semesterSchedules", "semesterSchedules.classroom"})
    @Query("SELECT s FROM Semester s "
            + "WHERE :monday BETWEEN s.startDate AND s.endDate "
            + "OR :friday BETWEEN s.startDate AND s.endDate")
    List<Semester> findSemesterContainingAnyDate(LocalDate monday, LocalDate friday);

    @EntityGraph(attributePaths = {"semesterSchedules", "semesterSchedules.classroom"})
    @Query("SELECT s FROM Semester s WHERE :date BETWEEN s.startDate AND s.endDate")
    List<Semester> findSemesterContainingDate(LocalDate date);

    //이거 여러개 반환이지만 객체를 단일객체로 만들면 맨 위에 하나만 반환
    //끝난 학기중 가장 최신? 인 학기 조회
    @Query("SELECT s FROM Semester s WHERE s.endDate < :today ORDER BY s.endDate DESC LIMIT 1")
    Semester findTopByEndDateBeforeOrderByEndDateDesc(@Param("today") LocalDate today);
}