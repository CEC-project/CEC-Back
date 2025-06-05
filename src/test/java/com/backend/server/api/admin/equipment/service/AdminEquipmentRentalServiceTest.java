package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentBrokenOrRepairRequest;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentBrokenOrRepairRequest.EquipmentStatusActionType.BROKEN;
import static com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentBrokenOrRepairRequest.EquipmentStatusActionType.REPAIR;
import static com.backend.server.model.entity.enums.Status.IN_USE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminEquipmentRentalServiceTest {

    @InjectMocks
    private AdminEquipmentService adminEquipmentService;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquipmentBrokenHistoryRepository equipmentBrokenHistoryRepository;

    @Mock
    private EquipmentRepairHistoryRepository equipmentRepairHistoryRepository;

    private final Long equipmentId = 1L;
    private final Long userId = 1L;
    private final String detail = "테스트 상세내용";

    private Equipment equipment;
    private User adminUser;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
                .id(equipmentId)
                .status(IN_USE)
                .build();

        adminUser = User.builder()
                .id(userId)
                .name("관리자")
                .build();
    }

    @Test
    void changeStatus_toBroken_success() {
        // given
        AdminEquipmentBrokenOrRepairRequest request = new AdminEquipmentBrokenOrRepairRequest(
                List.of(equipmentId), BROKEN, detail);
        LoginUser loginUser = LoginUser.builder()
                .id(1L)
                .name("테스트맨")
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

        // when
        List<Long> result = adminEquipmentService.changeStatus(request, loginUser);

        // then
        verify(equipmentBrokenHistoryRepository).save(any(EquipmentBrokenHistory.class));
        verify(equipmentRepository).save(any(Equipment.class));
        assertThat(result).containsExactly(equipmentId);
    }

    @Test
    void changeStatus_toRepair_success() {
        // given
        equipment = equipment.toBuilder().status(Status.BROKEN).build();
        EquipmentBrokenHistory brokenHistory = EquipmentBrokenHistory.builder()
                .id(1L)
                .equipment(equipment)
                .brokenDetail("고장 사유")
                .build();

        AdminEquipmentBrokenOrRepairRequest request = new AdminEquipmentBrokenOrRepairRequest(
                List.of(equipmentId),REPAIR, detail);
        LoginUser loginUser = LoginUser.builder()
                .id(1L)
                .name("테스트맨")
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentBrokenHistoryRepository.findTopByEquipmentOrderByCreatedAtDesc(equipment))
                .thenReturn(Optional.of(brokenHistory));

        // when
        List<Long> result = adminEquipmentService.changeStatus(request, loginUser);

        // then
        verify(equipmentRepairHistoryRepository).save(any(EquipmentRepairHistory.class));
        verify(equipmentRepository).save(any(Equipment.class));
        assertThat(result).containsExactly(equipmentId);
    }
}
