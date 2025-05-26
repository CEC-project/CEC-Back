
package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import com.backend.server.api.admin.equipment.dto.equipment.response.*;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.*;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminEquipmentServiceTest {

    @InjectMocks
    private AdminEquipmentService adminEquipmentService;

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
        AdminEquipmentCreateRequest request = new AdminEquipmentCreateRequest();
        request.setCategoryId(1L);
        request.setModelId(1L);

        EquipmentCategory category = mock(EquipmentCategory.class);
        EquipmentModel model = mock(EquipmentModel.class);

        when(category.getEnglishCode()).thenReturn("CAMERA");
        when(model.getEnglishCode()).thenReturn("SONYA7");
        when(equipmentCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(equipmentModelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(equipmentRepository.countByEquipmentModel_Id(1L)).thenReturn(5L);

        String serial = adminEquipmentService.generateSerialNumber(request);
        System.out.println(serial);
        assertTrue(serial.startsWith("CAMSON"));
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


    @Test
    void approveRentalRequests_shouldSendNotification() {
        Long id = 1L;
        User user = User.builder().id(10L).build();
        Equipment equipment = Equipment.builder().status(Status.RENTAL_PENDING).renter(user).equipmentModel(EquipmentModel.builder().name("TestModel").build()).build();

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        adminEquipmentService.approveRentalRequests(List.of(id));
        verify(notificationService).createNotification(any(CommonNotificationDto.class), eq(10L));
    }

    @Test
    void markEquipmentsAsBroken_shouldUpdateStatus() {
        Long id = 1L;
        Equipment equipment = Equipment.builder().status(Status.AVAILABLE).description("Desc").brokenCount(0L).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        adminEquipmentService.markEquipmentsAsBroken(List.of(id), "Break note");
        verify(equipmentRepository).save(any());
    }

    @Test
    void repairEquipments_shouldUpdateStatus() {
        Long id = 1L;
        Equipment equipment = Equipment.builder().status(Status.BROKEN).description("Broken").build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        adminEquipmentService.repairEquipments(List.of(id), "Repair note");
        verify(equipmentRepository).save(any());
    }

    @Test
    void extendRentalPeriods_shouldUpdateDate() {
        Long id = 1L;
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        Equipment equipment = Equipment.builder().status(Status.IN_USE).endRentDate(LocalDateTime.now()).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        adminEquipmentService.extendRentalPeriods(List.of(id), newDate);
        verify(equipmentRepository).save(any());
    }

    @Test
    void forceReturnEquipments_shouldClearRentalInfo() {
        Long id = 1L;
        Equipment equipment = Equipment.builder().status(Status.IN_USE).renter(new User()).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));

        adminEquipmentService.forceReturnEquipments(List.of(id));
        verify(equipmentRepository).save(any());
    }


    @Test
    void getEquipments_shouldFilterByCategory() {
        AdminEquipmentListRequest request = AdminEquipmentListRequest.builder().build();
        request.setCategoryId(1L);

        Equipment equipment = Equipment.builder()
                .id(1L)
                .serialNumber("CAM123")
                .equipmentModel(EquipmentModel.builder().name("Canon EOS").build())
                .status(Status.AVAILABLE)
                .renter(User.builder().name("철수짱").build())
                .build();

        Page<Equipment> page = new PageImpl<>(List.of(equipment));
        when(equipmentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        AdminEquipmentListResponse result = adminEquipmentService.getEquipments(request);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getEquipments_shouldFilterByStatus(){
        AdminEquipmentListRequest request = AdminEquipmentListRequest.builder().build();
        request.setStatus("AVAILABLE");

        Equipment equipment = Equipment.builder()
                .id(1L)
                .serialNumber("CAM123")
                .equipmentModel(EquipmentModel.builder().name("Canon EOS").build())
                .status(Status.AVAILABLE)
                .renter(User.builder().name("철수짱").build())
                .build();

        Pageable pageable = EquipmentSpecification.getPageable(request);

    }

}
