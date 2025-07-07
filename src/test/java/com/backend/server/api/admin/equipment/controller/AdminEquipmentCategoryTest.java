package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;
import com.backend.server.config.ControllerTest;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.util.MockMvcUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.server.fixture.EquipmentCategoryFixture.장비분류1;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("장비 카테고리 컨트롤러 테스트")
public class AdminEquipmentCategoryTest {
    @Autowired
    private MockMvcUtil mockMvcUtil;
    @Autowired
    private MockMvc mockMvc;
    @Autowired private EquipmentCategoryRepository equipmentCategoryRepository;
    @Nested
    class 장비_카테고리_생성_API는{
        @Test
        void 이미_존재하는_카테고리_생성에_실패한다() throws Exception {
            //given
            equipmentCategoryRepository.save(장비분류1.엔티티_생성());
            AdminEquipmentCategoryCreateRequest request =  AdminEquipmentCategoryCreateRequest.builder()
                    .name(장비분류1.getName())
                    .maxRentalCount(장비분류1.getMaxRentalCount())
                    .englishCode(장비분류1.getEnglishCode())
                    .build();

            //when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-categories", request)
            );
            System.out.println(result);
            //then
            result.andExpect(status().is4xxClientError())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.success", true))
                    .andExpect(MockMvcUtil.jsonPathEquals("$.message", "이미 존재하는 카테고리 이름입니다."));
        }

    }


}
