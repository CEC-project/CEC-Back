package com.backend.server.api.user.equipment.service;

import com.backend.server.api.equipment.dto.category.EquipmentCategoryResponse;
import com.backend.server.api.equipment.service.EquipmentCategoryService;
import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentCategoryServiceTest {

    @InjectMocks
    private EquipmentCategoryService equipmentCategoryService;

    @Mock
    private EquipmentCategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_shouldReturnListOfResponses() {
        // given
        EquipmentCategory category1 = EquipmentCategory.builder().id(1L).name("카메라").build();
        EquipmentCategory category2 = EquipmentCategory.builder().id(2L).name("베터리").build();

        List<EquipmentCategory> categories = List.of(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        List<EquipmentCategoryResponse> result = equipmentCategoryService.getAllCategories();

        // then
        assertEquals(2, result.size());
        assertEquals("카메라", result.get(0).getName());
        assertEquals("베터리", result.get(1).getName());
    }

    @Test
    void getCategoryById_shouldReturnResponse() {
        // given
        EquipmentCategory category = EquipmentCategory.builder().id(1L).name("카메라").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // when
        EquipmentCategoryResponse result = equipmentCategoryService.getCategoryById(1L);

        // then
        assertEquals("카메라", result.getName());
    }

    @Test
    void getCategoryById_shouldThrowExceptionIfNotFound() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            equipmentCategoryService.getCategoryById(1L);
        });

        assertTrue(exception.getMessage().contains("해당 카테고리가 존재하지 않습니다"));
    }
}
