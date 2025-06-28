package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.model.AdminEquipmentModelCreateRequest;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminEquipmentModelServiceTest {

    @Mock
    private EquipmentModelRepository modelRepository;

    @Mock
    private EquipmentCategoryRepository categoryRepository;

    @InjectMocks
    private AdminEquipmentModelService service;

    private AdminEquipmentModelCreateRequest req;

    @BeforeEach
    void setUp() {
        // 기본 요청 DTO 준비
        req = AdminEquipmentModelCreateRequest.builder()
                .name("ModelX")
                .englishCode("MX")
                .categoryId(100L)
                .available(true)
                .build();
    }

    @Test
    @DisplayName("createModel: 정상 생성 후 ID 반환")
    void createModel_success() {
        // 중복 검사 목 설정
        when(modelRepository.existsByName("ModelX")).thenReturn(false);
        when(modelRepository.existsByEnglishCode("MX")).thenReturn(false);
        when(categoryRepository.existsById(100L)).thenReturn(true);

        EquipmentCategory savedCategory = EquipmentCategory.builder()
                .id(100L)
                .name("카메라")
                .englishCode("CAMERA").build();
        when(categoryRepository.findById(100L)).thenReturn(Optional.of(savedCategory));
        // 저장 결과 목 설정
        EquipmentModel saved = EquipmentModel.builder()
                .id(1L)
                .name("ModelX")
                .englishCode("MX")
                .category(savedCategory)
                .available(true)
                .build();
        when(modelRepository.save(any(EquipmentModel.class))).thenReturn(saved);

        // 실행
        Long responseId = service.createModel(req);

        // 검증
        assertThat(responseId).isEqualTo(1L);
        verify(modelRepository).existsByName("ModelX");
        verify(modelRepository).existsByEnglishCode("MX");
        verify(categoryRepository).existsById(100L);
        verify(modelRepository).save(any(EquipmentModel.class));
    }

    @Test
    @DisplayName("createModel: 이름 중복 시 예외")
    void createModel_duplicateName_throws() {
        when(modelRepository.existsByName("ModelX")).thenReturn(true);

        assertThatThrownBy(() -> service.createModel(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 모델 이름입니다.");

        verify(modelRepository).existsByName("ModelX");
        verify(modelRepository, never()).save(any());
    }

    @Test
    @DisplayName("createModel: 영문 코드 중복 시 예외")
    void createModel_duplicateCode_throws() {
        when(modelRepository.existsByName("ModelX")).thenReturn(false);
        when(modelRepository.existsByEnglishCode("MX")).thenReturn(true);

        assertThatThrownBy(() -> service.createModel(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 영문 코드입니다.");

        verify(modelRepository).existsByEnglishCode("MX");
        verify(modelRepository, never()).save(any());
    }

    @Test
    @DisplayName("createModel: 카테고리 미존재 시 예외")
    void createModel_categoryNotFound_throws() {
        when(modelRepository.existsByName("ModelX")).thenReturn(false);
        when(modelRepository.existsByEnglishCode("MX")).thenReturn(false);
        when(categoryRepository.existsById(100L)).thenReturn(false);

        assertThatThrownBy(() -> service.createModel(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 카테고리입니다. id=100");

        verify(categoryRepository).existsById(100L);
        verify(modelRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateModel: 정상 수정 후 ID 반환")
    void updateModel_success() {
        Long id = 10L;

        // 중복 검사 목 설정
        when(modelRepository.existsByNameAndIdNot("ModelX", id)).thenReturn(false);
        when(modelRepository.existsByEnglishCodeAndIdNot("MX", id)).thenReturn(false);
        when(categoryRepository.existsById(100L)).thenReturn(true);
        EquipmentCategory savedCategory = EquipmentCategory.builder()
                .id(100L)
                .name("카메라")
                .englishCode("CAMERA").build();
        when(categoryRepository.findById(100L)).thenReturn(Optional.of(savedCategory));

        // 기존 엔티티 조회 목 설정
        EquipmentModel existing = EquipmentModel.builder()
                .id(id)
                .name("OldModel")
                .englishCode("OLD")
                .category(savedCategory)
                .available(false)
                .build();
        when(modelRepository.findById(id)).thenReturn(Optional.of(existing));

        // 저장 결과 목 설정
        EquipmentModel updated = existing.toBuilder()
                .name("ModelX")
                .englishCode("MX")
                .category(savedCategory)
                .available(true)
                .build();
        when(modelRepository.save(any(EquipmentModel.class))).thenReturn(updated);

        // 실행
        Long responseId = service.updateModel(id, req);

        // 검증
        assertThat(responseId).isEqualTo(id);
        verify(modelRepository).findById(id);
        verify(modelRepository).existsByNameAndIdNot("ModelX", id);
        verify(modelRepository).existsByEnglishCodeAndIdNot("MX", id);
        verify(categoryRepository).existsById(100L);
        verify(modelRepository).save(any(EquipmentModel.class));
    }

    @Test
    @DisplayName("updateModel: 모델 미존재 시 예외")
    void updateModel_notFound_throws() {
        Long id = 20L;
        when(modelRepository.existsByNameAndIdNot("ModelX", id)).thenReturn(false);
        when(modelRepository.existsByEnglishCodeAndIdNot("MX", id)).thenReturn(false);
        when(categoryRepository.existsById(100L)).thenReturn(true);
        when(modelRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateModel(id, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 장비 모델이 존재하지 않습니다. id=" + id);
    }

    @Test
    @DisplayName("deleteModel: 정상 소프트 삭제 후 ID 반환")
    void deleteModel_success() {
        Long id = 30L;
        EquipmentModel existing = EquipmentModel.builder().id(id).build();
        when(modelRepository.findById(id)).thenReturn(Optional.of(existing));

        Long responseId = service.deleteModel(id);

        assertThat(responseId).isEqualTo(id);


        verify(modelRepository).save(existing);  // delete → save로 변경
    }

    @Test
    @DisplayName("deleteModel: 모델 미존재 시 예외")
    void deleteModel_notFound_throws() {
        Long id = 40L;
        when(modelRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteModel(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 장비 모델이 존재하지 않습니다. id=" + id);
    }
}
