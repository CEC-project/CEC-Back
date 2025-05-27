package com.backend.server.api.user.equipment.dto.equipment;

import java.time.LocalDateTime;

import com.backend.server.model.entity.Equipment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentResponse {
    final private String modelName;
    final private LocalDateTime startRentDate;
    final private LocalDateTime endRentDate;

    //대여 안하면 이거 npe니까 null일때
    @JsonInclude(JsonInclude.Include.NON_NULL)
    final private String renterName;
    final private String status;
    final private LocalDateTime createdAt;
    final private String imageUrl;
    
    public EquipmentResponse(Equipment equipment) {
        this.modelName = equipment.getEquipmentModel().getName();
        this.startRentDate = equipment.getStartRentDate();
        this.endRentDate = equipment.getEndRentDate();
        this.renterName = equipment.getRenter() != null
                ? equipment.getRenter().getName()
                : null;
        this.status = equipment.getStatus().name();
        this.createdAt = equipment.getCreatedAt();
        this.imageUrl = equipment.getImageUrl();
    }

}
