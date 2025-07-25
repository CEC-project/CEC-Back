package com.backend.server.model.entity.classroom;

import com.backend.server.model.entity.BaseTimeEntity;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.equipment.Equipment;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
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
@Table(name = "semester_schedule", indexes = {@Index(name = "idx_deleted_at_semester_schedule", columnList = "deleted_at")})
@Builder(toBuilder = true)
@Entity
public class SemesterSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Professor professor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semesterSchedule")
    private List<Equipment> equipments;

    private Integer day;

    @Column(length = 20)
    private String name;
    private String color;

    private LocalTime startAt;
    private LocalTime endAt;
}