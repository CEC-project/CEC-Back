package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;
import com.backend.server.config.ControllerTest;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.support.MockMvcUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.server.fixture.EquipmentCategoryFixture.장비분류1;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("AdminEquipmentCategoryController")
public class AdminEquipmentCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private EquipmentCategoryRepository equipmentCategoryRepository;
    @Nested
    class 장비_카테고리_생성_API는{
        @Test
        @DisplayName("정상적인 요청으로 카테고리 생성에 성공한다")
        void 정상적인_요청으로_카테고리_생성에_성공한다() throws Exception {
            // given
            AdminEquipmentCategoryCreateRequest request = AdminEquipmentCategoryCreateRequest.builder()
                    .name("새로운 장비 카테고리")
                    .maxRentalCount(5)
                    .englishCode("NEW_EQUIPMENT")
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-categories", request)
            );

            // then
            result.andExpect(status().is2xxSuccessful())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "success"));
        }

        @Test
        @DisplayName("이미 존재하는 카테고리 이름으로 생성에 실패한다")
        void 이미_존재하는_카테고리_이름_생성에_실패한다() throws Exception {
            // given
            equipmentCategoryRepository.save(장비분류1.엔티티_생성());
            AdminEquipmentCategoryCreateRequest request = AdminEquipmentCategoryCreateRequest.builder()
                    .name(장비분류1.getName())
                    .maxRentalCount(200)
                    .englishCode("DIFFERENT_CODE")
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-categories", request)
            );

            // then
            result.andExpect(status().is4xxClientError())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "fail"));
        }

        @Test
        @DisplayName("이미 존재하는 카테고리 영문코드로 생성에 실패한다")
        void 이미_존재하는_카테고리_영문코드_생성에_실패한다() throws Exception {
            // given
            equipmentCategoryRepository.save(장비분류1.엔티티_생성());
            AdminEquipmentCategoryCreateRequest request = AdminEquipmentCategoryCreateRequest.builder()
                    .name("다른 카테고리 이름")
                    .maxRentalCount(123)
                    .englishCode(장비분류1.getEnglishCode())
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-categories", request)
            );

            // then
            result.andExpect(status().is4xxClientError())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "fail"));
        }

    }


}
