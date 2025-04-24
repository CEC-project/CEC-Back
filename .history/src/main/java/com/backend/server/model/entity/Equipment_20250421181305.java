package com.backend.server.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.backend.server.model.entity.enums.RentalStatus;

@Entity
@Table(name = "equipment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image_url;

    @Column
    private String category;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private String status;

    @Column
    private Boolean available;

    @Column
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String attachment;

    // 관리자 참조
    @Column(name = "manager_id")
    private Long managerId;
    
    @Column(name = "manager_name")
    private String managerName;

    // 대여 제한 학년인데 콤마로 구분해서 넣음
    @Column(name = "rental_restricted_grades")
    private String rentalRestrictedGrades;
    

    @Column
    private RentalStatus rentalStatus;

    @Column
    private LocalDateTime rentalTime;

    @Column
    private LocalDateTime returnTime;

    @Column
    private Integer renterId;

    
}        