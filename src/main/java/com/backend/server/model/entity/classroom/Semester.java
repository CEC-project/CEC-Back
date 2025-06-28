package com.backend.server.model.entity.classroom;

import com.backend.server.model.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "semester", indexes = {@Index(name = "idx_deleted_at_semester", columnList = "deleted_at")})
@Builder(toBuilder = true)
@Entity
public class Semester extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    private Integer year;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "semester", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SemesterSchedule> semesterSchedules = new ArrayList<>();

    @Override
    public void softDelete() {
        super.softDelete();
        if (semesterSchedules != null)
            for (SemesterSchedule semesterSchedule : semesterSchedules)
                semesterSchedule.softDelete();
    }
}