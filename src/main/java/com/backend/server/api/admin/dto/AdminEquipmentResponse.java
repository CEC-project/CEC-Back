package com.backend.server.api.admin.dto;

import com.backend.server.model.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentResponse {
    private Long id;
    private String name;
    private String category;
    private String modelName;
    private String status;
    private Integer quantity;
    private String description;
    private String attachment;
    
    private Long managerId;
    private String managerName;
    
    private String rentalStatus;  // enum 대신 String으로 변경
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private Integer renterId;
    
    private List<Integer> rentalRestrictedGrades;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Entity -> DTO 변환 생성자
    public AdminEquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.category = equipment.getCategory();
        this.modelName = equipment.getModelName();
        this.status = equipment.getStatus();
        this.quantity = equipment.getQuantity();
        this.description = equipment.getDescription();
        this.attachment = equipment.getAttachment();
        
        this.managerId = equipment.getManagerId();
        this.managerName = equipment.getManagerName();
        
        this.rentalStatus = equipment.getRentalStatus() != null ? 
                equipment.getRentalStatus().toString() : null;
                
        this.rentalTime = equipment.getRentalTime();
        this.returnTime = equipment.getReturnTime();
        this.renterId = equipment.getRenterId();
        
        // 대여제한학년 (1학년, 2학년) 이런식으로 있으면 , 를 기준으로 스플릿해서
        // DTO변환과정에서 List<Integer>로 변환해서 넣어줌
        //그럼 json에서는 배열로 나감.
        this.rentalRestrictedGrades = new ArrayList<>();
        String grades = equipment.getRentalRestrictedGrades();
        if (grades != null && !grades.isEmpty()) {
            this.rentalRestrictedGrades = Arrays.stream(grades.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        
        this.createdAt = equipment.getCreatedAt();
        this.updatedAt = equipment.getUpdatedAt();
    }
}