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
@Builder(toBuilder = true)
public class Equipment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String serialNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image_url;

    @Column(nullable = false)
    private Long equipmentModelId;

    // @Column(nullable = false)
    // private Long categoryId;

    @Column(nullable = false)
    private String modelName;

    @Column
    private Boolean available;

    @Column
    private Integer quantity;

    @Column
    private LocalDateTime rentalTime;

    @Column
    private LocalDateTime returnTime;

    @Column
    private Integer renterId;

    
    @Column
    private RentalStatus rentalStatus;

    @Column
    private Integer maxRentalCount;

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
    



    // 대여 상태 업데이트를 위한 Setter 메소드
    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }
    
    // 대여자 ID 업데이트를 위한 Setter 메소드
    public void setRenterId(Integer renterId) {
        this.renterId = renterId;
    }
    
    // 대여 시간 업데이트를 위한 Setter 메소드
    public void setRentalTime(LocalDateTime rentalTime) {
        this.rentalTime = rentalTime;
    }
    
    // 반납 시간 업데이트를 위한 Setter 메소드
    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }
    
    // 대여 가능 여부 업데이트를 위한 Setter 메소드
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}