package com.backend.server.model.entity;
import com.backend.server.api.admin.dto.AdminEquipmentResponse;
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

    @Column
    private String category;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private String status;

    // @Column
    // private Boolean available;

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

    public AdminEquipmentResponse toDto() {
        return AdminEquipmentResponse.builder()
                .id(this.id)
                .name(this.name)
                .category(this.category)
                .modelName(this.modelName)
                .status(this.status)
                .quantity(this.quantity)
                .description(this.description)
                .attachment(this.attachment)
                .managerId(this.managerId)
                .managerName(this.managerName)
                .rentalRestrictedGrades(this.rentalRestrictedGrades)
                .rentalStatus(this.rentalStatus != null ? this.rentalStatus.toString() : null)
                .rentalTime(this.rentalTime != null ? this.rentalTime.toString() : null)
                .returnTime(this.returnTime != null ? this.returnTime.toString() : null)
                .renterId(this.renterId)
                .build();
    }
}        