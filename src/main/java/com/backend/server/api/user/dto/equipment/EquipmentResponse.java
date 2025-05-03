// package com.backend.server.api.user.dto.equipment;

// import com.backend.server.model.entity.Equipment;
// import com.backend.server.model.entity.enums.Status;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;

// import java.time.LocalDateTime;

// @Getter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class EquipmentResponse {
//     private Long id;
//     private String name;
//     private Long categoryId;
//     private String modelName;
//     private Status rentalStatus;
//     private Boolean available;
//     private Integer quantity;
//     private Integer maxRentalCount;
//     private String description;
//     private String imageUrl;
//     private Boolean isFavorite;
//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt;
    

    
//     public EquipmentResponse(Equipment equipment) {
//         this.id = equipment.getId();
//         this.name = equipment.getName();
//         this.categoryId = equipment.getCategoryId();
//         this.modelName = equipment.getModelName();
//         this.rentalStatus = equipment.getRentalStatus();
//         this.available = equipment.getAvailable();
//         this.quantity = equipment.getQuantity();
//         this.maxRentalCount = equipment.getMaxRentalCount();     
//         this.description = equipment.getDescription();
//         this.imageUrl = equipment.getImage_url();
//         this.createdAt = equipment.getCreatedAt();
//         this.updatedAt = equipment.getUpdatedAt(); 
//     }

// }
