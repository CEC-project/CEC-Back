package com.backend.server.api.user.equipment.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentActionRequest;
import com.backend.server.model.entity.*;
import com.backend.server.model.entity.enums.EquipmentAction;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCart;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.EquipmentCartRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock private EquipmentRepository equipmentRepository;
    @Mock private UserRepository userRepository;
    @Mock private EquipmentCartRepository equipmentCartRepository;
    @Mock private CommonNotificationService notificationService;
    @InjectMocks private EquipmentService equipmentService;

    private LoginUser loginUser;
    private User user;
    private Equipment equipment;
    private EquipmentCategory category;

    @BeforeEach
    void setUp() {
        loginUser = LoginUser.builder().id(1L).grade(1).name("테스트유저").build();
        user = User.builder().id(1L).grade(1).name("테스트유저").build();
        category = EquipmentCategory.builder().name("카메라").englishCode("CAMERA").maxRentalCount(10).build();
        EquipmentModel model = EquipmentModel.builder().name("Canon").build();

        equipment = Equipment.builder()
                .id(101L)
                .equipmentCategory(category)
                .equipmentModel(model)
                .status(Status.AVAILABLE)
                .restrictionGrade("2,3,4")
                .build();
    }

    @Nested
    class AddToCartTests {
        @Test
        void addToCart_success() {
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.of(equipment));
            when(equipmentCartRepository.existsByUserIdAndEquipmentId(user.getId(), equipment.getId())).thenReturn(false);

            equipmentService.addToCart(loginUser, List.of(equipment.getId()));

            verify(equipmentCartRepository).save(any(EquipmentCart.class));
        }

        @Test
        void addToCart_gradeRestricted_throwsException() {
            user = user.toBuilder().grade(3).build(); // 제한된 학년
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.of(equipment));

            assertThrows(IllegalStateException.class, () ->
                    equipmentService.addToCart(loginUser, List.of(equipment.getId()))
            );
        }
    }

    @Nested
    class HandleUserActionTests {
        private EquipmentActionRequest request;
        private LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        private LocalDateTime endDate = LocalDateTime.now().plusDays(3);

        @BeforeEach
        void setupRequest() {
            request = EquipmentActionRequest.builder()
                    .equipmentIds(List.of(equipment.getId()))
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        }

        @Test
        void handleRentRequest_success() {
            when(userRepository.findById(loginUser.getId())).thenReturn(Optional.of(user));
            when(equipmentRepository.findByIdForUpdate(equipment.getId())).thenReturn(Optional.of(equipment));

            equipmentService.handleUserAction(loginUser, request, EquipmentAction.RENT_REQUEST);

            verify(equipmentRepository).save(any(Equipment.class));
            verify(equipmentCartRepository).deleteByUserIdAndEquipmentId(user.getId(), equipment.getId());
            verify(notificationService).createNotificationToAdmins(any(CommonNotificationDto.class));
        }

        @Test
        void handleRentCancel_success() {
            Equipment rented = equipment.toBuilder()
                    .status(Status.RENTAL_PENDING)
                    .renter(user)
                    .startRentTime(startDate)
                    .endRentTime(endDate)
                    .build();

            when(userRepository.findById(loginUser.getId())).thenReturn(Optional.of(user));
            when(equipmentRepository.findByIdForUpdate(equipment.getId())).thenReturn(Optional.of(rented));

            equipmentService.handleUserAction(loginUser, request, EquipmentAction.RENT_CANCEL);

            verify(equipmentRepository).save(any(Equipment.class));
        }

        @Test
        void handleReturnRequest_success() {
            Equipment inUse = equipment.toBuilder()
                    .status(Status.IN_USE)
                    .renter(user)
                    .build();

            when(userRepository.findById(loginUser.getId())).thenReturn(Optional.of(user));
            when(equipmentRepository.findByIdForUpdate(equipment.getId())).thenReturn(Optional.of(inUse));

            equipmentService.handleUserAction(loginUser, request, EquipmentAction.RETURN_REQUEST);

            verify(equipmentRepository).save(any(Equipment.class));
        }

        @Test
        void handleReturnCancel_success() {
            Equipment returnPending = equipment.toBuilder()
                    .status(Status.RETURN_PENDING)
                    .renter(user)
                    .build();

            when(userRepository.findById(loginUser.getId())).thenReturn(Optional.of(user));
            when(equipmentRepository.findByIdForUpdate(equipment.getId())).thenReturn(Optional.of(returnPending));

            equipmentService.handleUserAction(loginUser, request, EquipmentAction.RETURN_CANCEL);

            verify(equipmentRepository).save(any(Equipment.class));
        }
    }
}
