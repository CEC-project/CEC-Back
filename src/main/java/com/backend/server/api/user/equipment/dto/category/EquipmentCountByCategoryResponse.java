package com.backend.server.api.user.equipment.dto.category;

import com.backend.server.model.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentCountByCategoryResponse {
    private Long id;

    private String name;

    private Integer totalCount;

    private Integer available;

    private Integer maxRentalCount;

    private Integer brokenCount;

}
