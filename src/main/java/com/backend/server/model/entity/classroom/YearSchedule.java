package com.backend.server.model.entity.classroom;

import com.backend.server.model.entity.BaseTimeEntity;
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
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "year_schedule", indexes = {@Index(name = "idx_deleted_at_year_schedule", columnList = "deleted_at")})
@Builder(toBuilder = true)
@Entity
public class YearSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Classroom classroom;

    private LocalDate date;
    private Boolean isHoliday;

    @Column(length = 20)
    private String description;
    private String color;

    private LocalTime startAt;
    private LocalTime endAt;
}