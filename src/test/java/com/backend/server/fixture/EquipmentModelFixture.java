package com.backend.server.fixture;

import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EquipmentModelFixture {
    장비모델1("캐논 EOS R6",
            "CANON-EOS-R6",
            true,
            1),
    장비모델2("소니 A7IV",
            "SONY-A7IV",
            true,
            2),
    장비모델3("니콘 Z6II",
            "NIKON-Z6II",
            false,
            3);

    private final String name;
    private final String englishCode;
    private final boolean available;
    private final Integer modelGroupIndex;

    public EquipmentModel 엔티티_생성(EquipmentCategory category) {
        return EquipmentModel.builder()
                .name(name)
                .englishCode(englishCode)
                .available(available)
                .modelGroupIndex(modelGroupIndex)
                .category(category)
                .build();
    }
}