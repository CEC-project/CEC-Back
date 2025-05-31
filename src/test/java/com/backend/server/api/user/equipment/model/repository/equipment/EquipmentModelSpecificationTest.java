package com.backend.server.api.user.equipment.model.repository.equipment;

import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.config.AbstractPostgresContainerTest;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.equipment.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EquipmentModelSpecificationTest extends AbstractPostgresContainerTest {

    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    private EquipmentModelRepository equipmentModelRepository;


    @Autowired
    private EntityManager em;

    private EquipmentCategory savedCategory1;
    private EquipmentCategory savedCategory2;


    @BeforeEach
    void setUp(){
        savedCategory1 = equipmentCategoryRepository.save(EquipmentCategory.builder()
                .name("카메라")
                .englishCode("CAMERA")
                .maxRentalCount(10)
                .build());

        savedCategory2 = equipmentCategoryRepository.save(EquipmentCategory.builder()
                .name("마이크")
                .englishCode("MIC")
                .maxRentalCount(10)
                .build());

        EquipmentModel savedModel1 = equipmentModelRepository.save(EquipmentModel.builder()
                .name("SONY-A7000")
                .englishCode("SON")
                .category(savedCategory1)
                .build());

        EquipmentModel savedModel2 = equipmentModelRepository.save(EquipmentModel.builder()
                .name("SONY-A7500")
                .englishCode("SON")
                .category(savedCategory1)
                .build());

        EquipmentModel savedModel3 = equipmentModelRepository.save(EquipmentModel.builder()
                .name("SHURE-A1000")
                .englishCode("SHURE")
                .category(savedCategory2)
                .build());

        em.flush();
        em.clear();
    }

    @Test
    void specification_shouldFilterAllCondition() {
        // given
        EquipmentModelListRequest request = EquipmentModelListRequest.builder()
                .keyword("sony")
                .categoryId(savedCategory1.getId())
                .build();

        Specification<EquipmentModel> spec = EquipmentModelSpecification.filterEquipmentModels(request);

        // when
        List<EquipmentModel> result = equipmentModelRepository.findAll(spec);

        // then
        assertThat(result).hasSize(2);
        EquipmentModel found = result.get(0);
        assertThat(found.getName()).contains("SONY");
        assertThat(found.getCategory().getId()).isEqualTo(savedCategory1.getId());
    }
}
