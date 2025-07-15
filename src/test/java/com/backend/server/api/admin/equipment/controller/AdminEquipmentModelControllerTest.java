package com.backend.server.api.admin.equipment.controller;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.config.ControllerTest;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.util.MockMvcUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.server.fixture.EquipmentCategoryFixture.장비분류1;
import static com.backend.server.fixture.EquipmentModelFixture.장비모델1;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("AdminEquipmentModelController")
public class AdminEquipmentModelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EquipmentModelRepository equipmentModelRepository;
    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    private EquipmentCategory savedCategory;
    private EquipmentModel savedModel;

    @BeforeEach
    void setUp() {
        savedCategory = equipmentCategoryRepository.save(장비분류1.엔티티_생성());
        savedModel = equipmentModelRepository.save(장비모델1.엔티티_생성(savedCategory));
    }

    @Nested
    @DisplayName("장비 모델 생성 API는")
    class 장비_모델_생성_API는 {

        @Test
        @DisplayName("정상적인 요청으로 모델 생성에 성공한다")
        void 정상적인_요청으로_모델_생성에_성공한다() throws Exception {
            // given
            AdminEquipmentModelCreateRequest request = AdminEquipmentModelCreateRequest.builder()
                    .name("새로운 장비 모델")
                    .englishCode("NEW_MODEL")
                    .available(true)
                    .categoryId(savedCategory.getId())
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-models", request)
            );

            // then
            result.andExpect(status().isOk())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "success"))
                    .andExpect(MockMvcUtil.jsonPathEquals("$.message", "장비 모델 생성 성공"));
        }

        @Test
        @DisplayName("이미 존재하는 모델명으로 생성에 실패한다")
        void 이미_존재하는_모델명으로_생성에_실패한다() throws Exception {
            // given
            AdminEquipmentModelCreateRequest request = AdminEquipmentModelCreateRequest.builder()
                    .name(savedModel.getName())
                    .englishCode("DIFFERENT_CODE")
                    .available(true)
                    .categoryId(savedCategory.getId())
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-models", request)
            );

            // then
            result.andExpect(status().is4xxClientError())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "fail"));
        }

        @Test
        @DisplayName("이미 존재하는 영문코드로 생성에 실패한다")
        void 이미_존재하는_영문코드로_생성에_실패한다() throws Exception {
            // given
            AdminEquipmentModelCreateRequest request = AdminEquipmentModelCreateRequest.builder()
                    .name("다른 모델명")
                    .englishCode(savedModel.getEnglishCode())
                    .available(true)
                    .categoryId(savedCategory.getId())
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-models", request)
            );

            // then
            result.andExpect(status().is4xxClientError())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "fail"));
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 ID로 생성에 실패한다")
        void 존재하지_않는_카테고리_ID로_생성에_실패한다() throws Exception {
            // given
            AdminEquipmentModelCreateRequest request = AdminEquipmentModelCreateRequest.builder()
                    .name("새로운 모델")
                    .englishCode("NEW_CODE")
                    .available(true)
                    .categoryId(999L)
                    .build();

            // when
            ResultActions result = mockMvc.perform(
                    MockMvcUtil.postJson("/api/admin/equipment-models", request)
            );

            // then
            result.andExpect(status().is4xxClientError())
                    .andExpect(MockMvcUtil.jsonPathEquals("$.status", "fail"));
        }
    }
}
