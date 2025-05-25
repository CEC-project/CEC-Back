//package com.backend.server.api.user.equipment.service;
//
//import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
//import com.backend.server.api.user.equipment.dto.model.EquipmentModelListResponse;
//import com.backend.server.api.user.equipment.dto.model.EquipmentModelResponse;
//import com.backend.server.model.entity.EquipmentModel;
//import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
//import com.backend.server.model.repository.equipment.EquipmentModelRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//public class EquipmentModelServiceTest {
//    @InjectMocks
//    private EquipmentModelService equipmentModelService;
//
//    @Mock
//    private EquipmentModelRepository equipmentModelRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//    //상세조회 테스트
//    @Test
//    void getEquipmentModel_shouldReturnResponse() {
//
//        EquipmentModel model = EquipmentModel.builder().id(1L).name("SONY-A5000").build();
//        when(equipmentModelRepository.findById(1L)).thenReturn(Optional.of(model));
//
//        Optional<EquipmentModel> result = equipmentModelRepository.findById(1L);
//        assertTrue(result.isPresent());
//        assertEquals("SONY-A5000", result.get().getName());
//    }
//
//
//    //페이지 테스트
//    @Test
//    void getAllModels_shouldReturnFilteredResults() {
//        //1.given
//        EquipmentModel model1 = EquipmentModel.builder().id(1L).name("SONY-A7000").build();
//        EquipmentModel model2 = EquipmentModel.builder().id(1L).name("SONY-A8000").build();
//
//        EquipmentModelListRequest request = EquipmentModelListRequest.builder()
//                .keyword("sony")
//                .page(0)
//                .size(10).build();
//
//        Page<EquipmentModel> page = new PageImpl<>(List.of(model1,model2));
//        when(equipmentModelRepository.findAll(any(Specification.class), any(Pageable.class)))
//                .thenReturn(page);
//
//        //2.when
//        EquipmentModelListResponse result = equipmentModelService.getAllModels(request);
//        //3.then
//        assertEquals(2, result.getContent().size());
//        assertEquals("SONY-A7000", result.getContent().get(0).getName());
//
//    }
//
////    @Test
////    void getModelsByCategory_shouldReturnFilteredByCategoryResult() {
////        // 1. given
////        EquipmentModel model1 = EquipmentModel.builder().id(1L).name("SONY-A7000").categoryId(1L).build();
////        EquipmentModel model2 = EquipmentModel.builder().id(2L).name("SONY-A8000").categoryId(2L).build();
////
////        EquipmentModelListRequest request = EquipmentModelListRequest.builder()
////                .categoryId(1L)
////                .page(0)
////                .size(10)
////                .build();
////
////        Page<EquipmentModel> page = new PageImpl<>(List.of(model1, model2));
////        when(equipmentModelRepository.findAll(any(Specification.class), any(Pageable.class)))
////                .thenReturn(page);
////
////        // 2. when
////        EquipmentModelListResponse response = equipmentModelService.getAllModels(request);
////
////        // 3. then
////        assertEquals(2, response.getContent().size());
////        System.out.println();
////        assertEquals("SONY-A7000", response.getContent().get(0).getName());
////    }
//
//    @Test
//    void getModelsByCategory_shouldReturnFilteredBySearchResultAndCategoryId() {
//        // 1. given
//        EquipmentModel model1 = EquipmentModel.builder().id(1L).name("SONY-A7000").categoryId(1L).build();
//        EquipmentModel model2 = EquipmentModel.builder().id(1L).name("SONY-A7500").categoryId(1L).build();
//
//        EquipmentModel model3 = EquipmentModel.builder().id(2L).name("SONY-A8000").categoryId(2L).build();
//
//        EquipmentModelListRequest request = EquipmentModelListRequest.builder()
//                .categoryId(1L)
//                .keyword("75")
//                .page(0)
//                .size(10)
//                .build();
//
//        Page<EquipmentModel> page = new PageImpl<>(List.of(model1, model2, model3));
//        when(equipmentModelRepository.findAll(any(Specification.class), any(Pageable.class)))
//                .thenReturn(page);
//
//        // 2. when
//        EquipmentModelListResponse response = equipmentModelService.getAllModels(request);
//
//        // 3. then
//        assertEquals("SONY-A7500", response.getContent().get(0).getName());
//    }
//
//}
