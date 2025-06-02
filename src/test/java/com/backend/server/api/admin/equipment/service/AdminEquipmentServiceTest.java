
package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import com.backend.server.api.admin.equipment.dto.equipment.response.*;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.*;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminEquipmentServiceTest {

    @InjectMocks
    private AdminEquipmentService adminEquipmentService;

    @InjectMocks
    private AdminEquipmentRentalService adminEquipmentRentalService;

    @Mock private UserRepository userRepository;
    @Mock private EquipmentRepository equipmentRepository;
    @Mock private EquipmentCategoryRepository equipmentCategoryRepository;
    @Mock private EquipmentModelRepository equipmentModelRepository;
    @Mock private CommonNotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAdminUsers_shouldReturnList() {
        User admin = new User();
        when(userRepository.findByRoleIn(Role.ROLE_ADMIN, Role.ROLE_SUPER_ADMIN)).thenReturn(List.of(admin));

        List<AdminManagerCandidatesResponse> result = adminEquipmentService.getAdminUsers();
        assertEquals(1, result.size());
    }

    @Test
    void generateSerialNumber_shouldReturnFormattedCode() {
        AdminEquipmentSerialNumberGenerateRequest request = new AdminEquipmentSerialNumberGenerateRequest();
        request.setCategoryId(1L);
        request.setModelId(1L);

        EquipmentCategory category = mock(EquipmentCategory.class);
        EquipmentModel model = mock(EquipmentModel.class);

        when(category.getEnglishCode()).thenReturn("CA");
        when(model.getEnglishCode()).thenReturn("SONYA7");
        when(equipmentCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(equipmentModelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(equipmentRepository.countByEquipmentModel_Id(1L)).thenReturn(5L);

        String serial = adminEquipmentService.generateSerialNumber(request);
        System.out.println(serial);
        assertTrue(serial.startsWith("_CASON"));
    }

    @Test
    void deleteEquipment_shouldReturnId() {
        Long id = 1L;
        Long responseId= adminEquipmentService.deleteEquipment(id);
        assertEquals(id, responseId);
        verify(equipmentRepository).deleteById(id);
    }

    @Test
    void updateEquipmentStatus_shouldUpdateStatus() {
        Long id = 1L;
        Equipment equipment = Equipment.builder().status(Status.AVAILABLE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        AdminEquipmentStatusUpdateRequest request = AdminEquipmentStatusUpdateRequest.builder()
                .status(Status.BROKEN)
                .build();

        Long result = adminEquipmentService.updateEquipmentStatus(id, request);
        assertEquals(id, result);

        // 상태가 BROKEN으로 변경되었는지 검증
        verify(equipmentRepository).save(argThat(updated ->
                updated.getStatus() == Status.BROKEN
        ));
    }


}
