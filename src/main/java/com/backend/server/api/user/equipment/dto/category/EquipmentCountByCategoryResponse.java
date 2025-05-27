package com.backend.server.api.user.equipment.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentCountByCategoryResponse {
    private Long id;

    private String name;

    private String englishCode;

    private Integer totalCount;

    private Integer available;

    private Integer maxRentalCount;

    private Integer brokenCount;

}
