package com.backend.server.api.admin.equipment.service;

import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryCreateRequest;
import com.backend.server.api.admin.equipment.dto.category.AdminEquipmentCategoryIdResponse;
import com.backend.server.api.admin.equipment.service.AdminEquipmentCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// Mockito 확장을 활성화하여 @Mock, @InjectMocks 기능 사용
@ExtendWith(MockitoExtension.class)
class AdminEquipmentCategoryServiceTest {

    // EquipmentCategoryRepository를 모의(Mock) 객체로 생성
    @Mock
    EquipmentCategoryRepository categoryRepository;

    // 위의 mock 객체를 AdminEquipmentCategoryService에 주입
    @InjectMocks
    AdminEquipmentCategoryService service;

    // 테스트에 사용할 요청 DTO
    private AdminEquipmentCategoryCreateRequest req;

    // 각 테스트 메서드 실행 전마다 공통으로 수행되는 초기화 작업
    @BeforeEach
    void setUp() {
        // 기본 요청 데이터를 설정: 이름, 코드, 최대 대여 수
        req = AdminEquipmentCategoryCreateRequest.builder()
                .name("Camera")
                .englishCode("CAM")
                .maxRentalCount(5)
                .build();
    }

    // createCategory 메서드의 정상 흐름을 검증하는 테스트
    @Test
    void createCategory_success() {
        // 중복 이름 검사 시 false 반환 (중복 없음)
        when(categoryRepository.existsByName("Camera")).thenReturn(false);
        // 중복 코드 검사 시 false 반환 (중복 없음)
        when(categoryRepository.existsByEnglishCode("CAM")).thenReturn(false);

        // save 호출 시 리턴할 리턴값 설정: id=1L인 엔티티 생성
        EquipmentCategory saved = EquipmentCategory.builder()
                .id(1L)
                .name("Camera")
                .englishCode("CAM")
                .maxRentalCount(5)
                .build();
        when(categoryRepository.save(any())).thenReturn(saved);

        // service 호출 (실제 테스트 대상)
        Long responseId = service.createCategory(req);

        // 반환된 ID가 1L인지 검증
        assertThat(responseId).isEqualTo(1L);
        // 중복 검사 메서드 호출 여부 검증
        verify(categoryRepository).existsByName("Camera");
        verify(categoryRepository).existsByEnglishCode("CAM");
        // save 호출 여부 검증
        verify(categoryRepository).save(any(EquipmentCategory.class));
    }

    // createCategory 시 이름 중복 예외 처리 검증
    @Test
    void createCategory_duplicateName_throws() {
        // existsByName만 true 반환하도록 설정: 이름 중복 상황
        when(categoryRepository.existsByName("Camera")).thenReturn(true);

        // 예외가 발생하는지, 메시지 내용 검증
        assertThatThrownBy(() -> service.createCategory(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 카테고리 이름입니다.");

        // 이름 중복 검사만 호출되고, save는 호출되지 않아야 함
        verify(categoryRepository).existsByName("Camera");
        verify(categoryRepository, never()).save(any());
    }

    // updateCategory 메서드의 정상 흐름을 검증하는 테스트
    @Test
    void updateCategory_success() {
        Long id = 10L;
        // 업데이트 시 이름/코드 중복 검사: false 반환
        when(categoryRepository.existsByNameAndIdNot("Camera", id)).thenReturn(false);
        when(categoryRepository.existsByEnglishCodeAndIdNot("CAM", id)).thenReturn(false);

        // 기존 카테고리 엔티티 반환 설정
        EquipmentCategory existing = EquipmentCategory.builder()
                .id(id).name("Old").englishCode("OLD").maxRentalCount(3).build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));

        // 업데이트 후 저장될 엔티티 예시 객체
        EquipmentCategory updated = existing.toBuilder()
                .name("Camera").englishCode("CAM").maxRentalCount(5).build();

        // any matcher를 사용해 save 호출 시 updated 반환
        when(categoryRepository.save(any(EquipmentCategory.class))).thenReturn(updated);

        // service 호출
        Long responseId = service.updateCategory(id, req);

        // 반환 ID 검증
        assertThat(responseId).isEqualTo(id);
        // findById, save 호출 여부 검증
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(any(EquipmentCategory.class));
    }

    // deleteCategory 정상 흐름 검증
    @Test
    void deleteCategory_success() {
        Long id = 20L;
        // findById 시 엔티티 반환
        EquipmentCategory existing = EquipmentCategory.builder().id(id).build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));

        // service 호출
        Long responseId = service.deleteCategory(id);

        // 반환 ID 검증
        assertThat(responseId).isEqualTo(id);
        // delete 호출 여부 검증
        verify(categoryRepository).delete(existing);
    }

    // deleteCategory에서 엔티티가 없을 때 예외 처리 검증
    @Test
    void deleteCategory_notFound_throws() {
        Long id = 30L;
        // findById 시 빈 Optional 반환
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // 예외 발생 및 메시지 검증
        assertThatThrownBy(() -> service.deleteCategory(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 카테고리가 존재하지 않습니다. id=" + id);
    }
}
