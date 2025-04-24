package com.backend.server.api.user.dto;

import com.backend.server.model.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEquipmentResponse {
    private Long id;
    private String name;
    private String category;
    private String modelName;
    private String status;
    private Boolean available;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private Boolean isFavorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    

    
    public AdminEquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.category = equipment.getCategory();
        this.modelName = equipment.getModelName();
        this.status = equipment.getStatus();
        this.available = equipment.getAvailable();
        this.quantity = equipment.getQuantity();
        this.description = equipment.getDescription();
        this.imageUrl = equipment.getImage_url();
        this.createdAt = equipment.getCreatedAt();
        this.updatedAt = equipment.getUpdatedAt(); 
    }

}
