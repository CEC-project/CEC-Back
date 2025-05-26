package com.backend.server.api.user.equipment.model.repository.equipment;

import com.backend.server.api.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.model.repository.equipment.EquipmentModelSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest //h2 인메모리 활성화 환경 자동 구성하는 어노테이션
@TestPropertySource(properties = "spring.sql.init.mode=never") //더미데이터 생성 막기

class EquipmentModelSpecificationTest {

    @Autowired
    EquipmentModelRepository equipmentModelRepository;
    @Autowired
    EquipmentCategoryRepository equipmentCategoryRepository;

    @Test
    void specification_shouldFilterByKeywordAndCategory() {
        // given
        //카테고리 2개 만들어서 테스트
        EquipmentCategory c1 = EquipmentCategory.builder().name("카메라").englishCode("CAMERA").maxRentalCount(10).build();
        EquipmentCategory c2 = EquipmentCategory.builder().name("삼각대").englishCode("TRIPOD").maxRentalCount(10).build();

        equipmentCategoryRepository.saveAll(List.of(c1,c2));

        EquipmentModel m1 = EquipmentModel.builder().name("SONY-A7000").englishCode("SON").category(c1).build();
        EquipmentModel m2 = EquipmentModel.builder().name("SONY-A7500").englishCode("SON").category(c1).build();
        EquipmentModel m3 = EquipmentModel.builder().name("CANON-Z5").englishCode("CAN").category(c2).build();

        equipmentModelRepository.saveAll(List.of(m1, m2, m3));

        EquipmentModelListRequest request = EquipmentModelListRequest.builder()
                .categoryId(1L)
                .keyword("75")
                .build();

        Specification<EquipmentModel> spec = EquipmentModelSpecification.filterEquipmentModels(request);
        List<EquipmentModel> result = equipmentModelRepository.findAll(spec);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("SONY-A7500");
    }
}
