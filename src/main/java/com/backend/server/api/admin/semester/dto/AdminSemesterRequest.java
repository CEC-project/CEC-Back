package com.backend.server.api.admin.semester.dto;

import com.backend.server.model.entity.classroom.Semester;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AdminSemesterRequest {
    @Min(2000)
    @Max(2100)
    @NotNull
    private Integer year;

    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public Semester toEntity() {
        return Semester.builder()
                .year(year)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}