package com.backend.server.fixture;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum EquipmentCategoryFixture {
    장비분류1("장비분류1",
            10,
            "EquipmentCategory"),
    장비분류2("장비분류2",
            20,
            "EquipmentCategory2"),
    장비분류3("장비분류3",
            30,
            "EquipmentCategory3");

    private final String name;
    private final Integer maxRentalCount;
    private final String englishCode;
    public EquipmentCategory 엔티티_생성(){
        return EquipmentCategory.builder()
                .name(name)
                .maxRentalCount(maxRentalCount)
                .englishCode(englishCode)
                .build();
    }

}
