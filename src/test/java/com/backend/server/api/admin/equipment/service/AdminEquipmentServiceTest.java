package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminManagerCandidatesResponse;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.*;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminEquipmentServiceTest {

    @Mock UserRepository userRepository;
    @Mock EquipmentRepository equipmentRepository;
    @Mock EquipmentCategoryRepository categoryRepository;
    @Mock EquipmentModelRepository modelRepository;
    @Mock CommonNotificationService notificationService;

    @InjectMocks AdminEquipmentService service;

    private EquipmentCategory category;
    private EquipmentModel model;

    @BeforeEach
    void setUp() {
        // 공통 카테고리·모델 목 객체
        category = EquipmentCategory.builder()
                .id(1L).englishCode("CAT01").build();
        model = EquipmentModel.builder()
                .id(2L).englishCode("MOD01").build();
    }

    @Test
    @DisplayName("getAdminUsers: ROLE_ADMIN, ROLE_SUPER_ADMIN 조회")
    void getAdminUsers_success() {
        User u1 = User.builder().id(10L).role(Role.ROLE_ADMIN).build();
        User u2 = User.builder().id(11L).role(Role.ROLE_SUPER_ADMIN).build();
        when(userRepository.findByRoleIn(Role.ROLE_ADMIN, Role.ROLE_SUPER_ADMIN))
                .thenReturn(List.of(u1, u2));

        List<AdminManagerCandidatesResponse> list = service.getAdminUsers();

        assertThat(list).hasSize(2)
                .extracting(AdminManagerCandidatesResponse::getUser_id)
                .containsExactly(10L, 11L);
        verify(userRepository).findByRoleIn(Role.ROLE_ADMIN, Role.ROLE_SUPER_ADMIN);
    }

    @Test
    @DisplayName("generateSerialNumber: prefix + count 포맷")
    void generateSerialNumber_success() {
        AdminEquipmentCreateRequest req = new AdminEquipmentCreateRequest(
                1L, 2L, 0, null, null, null, null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelRepository.findById(2L)).thenReturn(Optional.of(model));
        when(equipmentRepository.countByEquipmentModel_Id(2L)).thenReturn(42L);

        String serial = service.generateSerialNumber(req);

        assertThat(serial).isEqualTo("CATMOD0042");
    }

    @Test
    @DisplayName("createEquipment: quantity=2일 때 두 개 생성")
    void createEquipment_multiple_success() {
        AdminEquipmentCreateRequest req = new AdminEquipmentCreateRequest(
                1L, 2L, 2, "url", 100L, "desc", 5);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelRepository.findById(2L)).thenReturn(Optional.of(model));
        when(equipmentRepository.countByEquipmentModel_Id(2L)).thenReturn(5L);

        Equipment e1 = Equipment.builder().id(100L).build();
        Equipment e2 = Equipment.builder().id(101L).build();
        when(equipmentRepository.save(any())).thenReturn(e1, e2);

        AdminEquipmentIdsResponse resp = service.createEquipment(req);

        assertThat(resp.getIds()).containsExactly(100L, 101L);
        verify(equipmentRepository, times(2)).save(any(Equipment.class));
    }

    @Test
    @DisplayName("updateEquipment: 정상 업데이트 후 ID 반환")
    void updateEquipment_success() {
        Long id = 50L;
        AdminEquipmentCreateRequest req = new AdminEquipmentCreateRequest(
                null, null, 0, "newUrl", 200L, "newDesc", 0);
        Equipment existing = Equipment.builder()
                .id(id).imageUrl("old").managerId(100L).description("oldDesc").restrictionGrade(1).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(existing));

        AdminEquipmentIdResponse resp = service.updateEquipment(id, req);

        assertThat(resp.getId()).isEqualTo(id);
        ArgumentCaptor<Equipment> cap = ArgumentCaptor.forClass(Equipment.class);
        verify(equipmentRepository).save(cap.capture());
        Equipment saved = cap.getValue();
        assertThat(saved.getImageUrl()).isEqualTo("newUrl");
        assertThat(saved.getManagerId()).isEqualTo(200L);
        assertThat(saved.getDescription()).isEqualTo("newDesc");
    }

    @Test
    @DisplayName("deleteEquipment: 정상 삭제 후 ID 반환")
    void deleteEquipment_success() {
        Long id = 60L;

        AdminEquipmentIdResponse resp = service.deleteEquipment(id);

        assertThat(resp.getId()).isEqualTo(id);
        verify(equipmentRepository).deleteById(id);
    }

    @Test
    @DisplayName("getEquipments: Pageable과 Specification 조합")
    void getEquipments_success() {
        AdminEquipmentListRequest req = new AdminEquipmentListRequest();
        // 페이징·필터용 목 설정 (간단히 빈 페이지)
        Page<Equipment> page = new PageImpl<>(List.of());
        when(equipmentRepository.findAll(any(), any(Pageable.class))).thenReturn(page);

        AdminEquipmentListResponse resp = service.getEquipments(req);

        assertThat(resp.getEquipments()).isEmpty();
        assertThat(resp.getTotalCount()).isEqualTo(0);
        verify(equipmentRepository).findAll(any(), any(Pageable.class));
    }

    @Test
    @DisplayName("getEquipment: ID로 조회 성공")
    void getEquipment_success() {
        Long id = 70L;
        Equipment existing = Equipment.builder().id(id).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(existing));

        AdminEquipmentResponse resp = service.getEquipment(id);

        assertThat(resp.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("updateEquipmentStatus: 정상 변경 후 ID 반환")
    void updateEquipmentStatus_success() {
        Long id = 80L;
        AdminEquipmentStatusUpdateRequest req = new AdminEquipmentStatusUpdateRequest(Status.BROKEN);
        Equipment ex = Equipment.builder().id(id).status(Status.AVAILABLE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(ex));

        Long returned = service.updateEquipmentStatus(id, req);

        assertThat(returned).isEqualTo(id);
        ArgumentCaptor<Equipment> cap = ArgumentCaptor.forClass(Equipment.class);
        verify(equipmentRepository).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(Status.BROKEN);
    }

    @Test
    @DisplayName("approveRentalRequests: pending 장비 승인 및 알림")
    void approveRentalRequests_success() {
        Long id = 90L;
        User renter = User.builder().id(5L).build();
        Equipment eq = Equipment.builder()
                .id(id)
                .status(Status.RENTAL_PENDING)
                .renter(renter)
                .equipmentModel(EquipmentModel.builder().name("X").build())
                .build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.approveRentalRequests(List.of(id));

        ArgumentCaptor<Equipment> cap = ArgumentCaptor.forClass(Equipment.class);
        verify(equipmentRepository).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(Status.IN_USE);
        verify(notificationService).createNotification(any(CommonNotificationDto.class), eq(5L));
    }

    @Test
    @DisplayName("rejectRentalRequests: pending 아닌 경우 예외")
    void rejectRentalRequests_invalidState_throws() {
        Long id = 95L;
        Equipment eq = Equipment.builder().id(id).status(Status.AVAILABLE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        assertThatThrownBy(() -> service.rejectRentalRequests(List.of(id)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("거절할 수 있습니다. ID: " + id);
    }

    @Test
    @DisplayName("rejectRentalRequests: pending 장비 거절 및 알림")
    void rejectRentalRequests_success() {
        Long id = 96L;
        User renter = User.builder().id(6L).build();
        Equipment eq = Equipment.builder()
                .id(id)
                .status(Status.RENTAL_PENDING)
                .renter(renter)
                .equipmentModel(EquipmentModel.builder().name("X").build())
                .description("orig")
                .build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.rejectRentalRequests(List.of(id));

        ArgumentCaptor<Equipment> cap = ArgumentCaptor.forClass(Equipment.class);
        verify(equipmentRepository).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(Status.AVAILABLE);
        verify(notificationService).createNotification(any(CommonNotificationDto.class), eq(6L));
    }

    @Test
    @DisplayName("processReturnRequests: invalid 상태 예외")
    void processReturnRequests_invalidState_throws() {
        Long id = 100L;
        Equipment eq = Equipment.builder().id(id).status(Status.BROKEN).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        assertThatThrownBy(() -> service.processReturnRequests(List.of(id)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("processReturnRequests: valid 상태 반납 처리")
    void processReturnRequests_success() {
        Long id = 101L;
        Equipment eq = Equipment.builder().id(id).status(Status.IN_USE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.processReturnRequests(List.of(id));

        verify(equipmentRepository).save(argThat(e -> e.getStatus() == Status.AVAILABLE));
    }

    @Test
    @DisplayName("markEquipmentsAsBroken: 고장 처리 및 brokenCount 증가")
    void markEquipmentsAsBroken_success() {
        Long id = 110L;
        Equipment eq = Equipment.builder()
                .id(id).status(Status.AVAILABLE).brokenCount(2L).description("orig").build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.markEquipmentsAsBroken(List.of(id), "픽스");

        ArgumentCaptor<Equipment> cap = ArgumentCaptor.forClass(Equipment.class);
        verify(equipmentRepository).save(cap.capture());
        Equipment saved = cap.getValue();
        assertThat(saved.getStatus()).isEqualTo(Status.BROKEN);
        assertThat(saved.getBrokenCount()).isEqualTo(3L);
        assertThat(saved.getDescription()).contains("픽스");
    }

    @Test
    @DisplayName("repairEquipments: 고장 아닌 경우 예외")
    void repairEquipments_invalidState_throws() {
        Long id = 120L;
        Equipment eq = Equipment.builder().id(id).status(Status.AVAILABLE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        assertThatThrownBy(() -> service.repairEquipments(List.of(id), "fix"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("repairEquipments: 정상 복구 처리")
    void repairEquipments_success() {
        Long id = 121L;
        Equipment eq = Equipment.builder().id(id).status(Status.BROKEN).description("orig").build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.repairEquipments(List.of(id), "repair note");

        verify(equipmentRepository).save(argThat(e ->
                e.getStatus() == Status.AVAILABLE && e.getDescription().contains("repair note")));
    }

    @Test
    @DisplayName("extendRentalPeriods: 과거 날짜 예외")
    void extendRentalPeriods_invalidDate_throws() {
        Long id = 130L;
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        assertThatThrownBy(() -> service.extendRentalPeriods(List.of(id), past))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("extendRentalPeriods: 정상 연장 처리")
    void extendRentalPeriods_success() {
        Long id = 131L;
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        Equipment eq = Equipment.builder().id(id).status(Status.IN_USE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.extendRentalPeriods(List.of(id), future);

        verify(equipmentRepository).save(argThat(e -> e.getEndRentDate().equals(future)));
    }

    @Test
    @DisplayName("forceReturnEquipments: 비사용 장비 예외")
    void forceReturnEquipments_invalidState_throws() {
        Long id = 140L;
        Equipment eq = Equipment.builder().id(id).status(Status.AVAILABLE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        assertThatThrownBy(() -> service.forceReturnEquipments(List.of(id)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("forceReturnEquipments: 정상 강제 회수")
    void forceReturnEquipments_success() {
        Long id = 141L;
        Equipment eq = Equipment.builder().id(id).status(Status.IN_USE).build();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(eq));

        service.forceReturnEquipments(List.of(id));

        verify(equipmentRepository).save(argThat(e -> e.getStatus()==Status.AVAILABLE));
    }

    @Test
    @DisplayName("getRentedEquipments: 대여 중인 장비 조회")
    void getRentedEquipments_success() {
        Equipment e1 = Equipment.builder().id(150L).status(Status.IN_USE).build();
        Equipment e2 = Equipment.builder().id(151L).status(Status.IN_USE).build();
        when(equipmentRepository.findByStatus(Status.IN_USE))
                .thenReturn(List.of(e1, e2));

        AdminEquipmentListResponse resp = service.getRentedEquipments();

        assertThat(resp.getEquipments()).hasSize(2)
                .extracting(AdminEquipmentResponse::getId)
                .containsExactlyInAnyOrder(150L, 151L);
    }
}
