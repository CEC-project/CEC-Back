//package com.backend.server.api.admin.equipment.controller;
//
//import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentCreateRequest;
//import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
//import com.backend.server.config.ControllerTest;
//import com.backend.server.fixture.EquipmentCategoryFixture;
//import com.backend.server.fixture.EquipmentFixture;
//import com.backend.server.fixture.EquipmentModelFixture;
//import com.backend.server.model.entity.equipment.Equipment;
//import com.backend.server.model.entity.equipment.EquipmentCategory;
//import com.backend.server.model.entity.equipment.EquipmentModel;
//import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
//import com.backend.server.model.repository.equipment.EquipmentModelRepository;
//import com.backend.server.model.repository.equipment.EquipmentRepository;
//import com.backend.server.util.MockMvcUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static com.backend.server.fixture.EquipmentCategoryFixture.장비분류1;
//import static com.backend.server.fixture.EquipmentFixture.장비1;
//import static com.backend.server.fixture.EquipmentModelFixture.장비모델1;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ControllerTest
//@DisplayName("장비 관리 컨트롤러 테스트")
//class AdminEquipmentControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private MockMvcUtil mockMvcUtil;
//    @Autowired private EquipmentRepository equipmentRepository;
//    @Autowired private EquipmentModelRepository equipmentModelRepository;
//    @Autowired private EquipmentCategoryRepository equipmentCategoryRepository;
//
//    private EquipmentCategory savedCategory;
//    private EquipmentModel savedModel;
//    private Equipment savedEquipment;
//
//    @BeforeEach
//    void setUp() {
//        savedCategory = equipmentCategoryRepository.save(장비분류1.엔티티_생성());
//        savedModel = equipmentModelRepository.save(장비모델1.엔티티_생성(savedCategory));
//        savedEquipment = equipmentRepository.save(장비1.엔티티_생성(savedCategory, savedModel));
//    }
//
//    @Nested
//    @DisplayName("장비 등록 API는")
//    class 장비_등록_API는 {
//        @Test
//        @DisplayName("정상적인 요청으로 장비 등록에 성공한다")
//        void 정상적인_요청으로_장비_등록_성공() throws Exception {
//            // given
//            AdminEquipmentCreateRequest request = AdminEquipmentCreateRequest.builder()
//                    .description("새로운 장비")
//                    .restrictionGrade("2")
//                    .imageUrl("https://example.com/image3.jpg")
//                    .categoryId(savedCategory.getId())
//                    .modelId(savedModel.getId())
//                    .build();
//
//            // when
//            ResultActions result = mockMvc.perform(
//                    MockMvcUtil.postJson("/api/admin/equipments", request)
//            );
//
//            // then
//            result.andExpect(status().isOk())
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.success", true))
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.message", "장비 등록 성공"));
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 카테고리로 등록시 실패한다")
//        void 존재하지_않는_카테고리로_실패() throws Exception {
//            AdminEquipmentCreateRequest request = AdminEquipmentCreateRequest.builder()
//                    .description("장비")
//                    .restrictionGrade("2")
//                    .imageUrl("https://example.com/image3.jpg")
//                    .categoryId(999L)
//                    .modelId(savedModel.getId())
//                    .build();
//
//            ResultActions result = mockMvc.perform(
//                    MockMvcUtil.postJson("/api/admin/equipments", request)
//            );
//
//            result.andExpect(status().is5xxServerError())
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.success", false));
//        }
//    }
//
//    @Nested
//    @DisplayName("장비 수정 API는")
//    class 장비_수정_API는 {
//        @Test
//        @DisplayName("정상적인 요청으로 장비 수정에 성공한다")
//        void 정상적인_요청으로_장비_수정_성공() throws Exception {
//            AdminEquipmentCreateRequest request = AdminEquipmentCreateRequest.builder()
//                    .description("수정된 장비")
//                    .restrictionGrade("2")
//                    .imageUrl("https://example.com/updated.jpg")
//                    .categoryId(savedCategory.getId())
//                    .modelId(savedModel.getId())
//                    .build();
//
//            ResultActions result = mockMvc.perform(
//                    MockMvcUtil.putJson("/api/admin/equipments/" + savedEquipment.getId(), request)
//            );
//
//            result.andExpect(status().isOk())
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.success", true))
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.message", "장비 수정 성공"));
//        }
//    }
//
//    @Nested
//    @DisplayName("장비 삭제 API는")
//    class 장비_삭제_API는 {
//        @Test
//        @DisplayName("정상적으로 장비 삭제에 성공한다")
//        void 장비_삭제_성공() throws Exception {
//            ResultActions result = mockMvc.perform(
//                    delete("/api/admin/equipments/" + savedEquipment.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//            );
//
//            result.andExpect(status().isOk())
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.success", true))
//                    .andExpect(MockMvcUtil.jsonPathEquals("$.message", "장비 삭제 성공"));
//            //소프트삭제는 직접검증
//            Equipment deletedEquipment = equipmentRepository.findById(savedEquipment.getId()).orElse(null);
//            assert deletedEquipment != null;
//            assertThat(deletedEquipment.getDeletedAt()).isNotNull();
//        }
//    }
//
//
//}
