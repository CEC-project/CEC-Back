package com.backend.server.api.user.equipment.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentRentalRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.model.entity.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock private EquipmentRepository equipmentRepository;
    @Mock private UserRepository userRepository;
    @Mock private EquipmentCartRepository equipmentCartRepository;
    @InjectMocks private EquipmentService equipmentService;

    private LoginUser loginUser;
    private User user;
    private Equipment equipment1;
    private Equipment equipment2;
    private EquipmentCategory category1;

    @BeforeEach
    void setUp() {
        loginUser = LoginUser.builder().id(1L).build();
        user = User.builder().id(1L).name("testuser").grade(1).build();
        category1 = EquipmentCategory.builder().name("카메라").englishCode("CAMERA").maxRentalCount(10).build();
        EquipmentModel model = EquipmentModel.builder().name("Canon 70D").build();

        equipment1 = Equipment.builder()
                .id(101L)
                .equipmentCategory(category1)
                .equipmentModel(model)
                .status(Status.AVAILABLE)
                .restrictionGrade("2,3,4")
                .build();

        equipment2 = Equipment.builder()
                .id(102L)
                .equipmentCategory(category1)
                .equipmentModel(model)
                .status(Status.AVAILABLE)
                .restrictionGrade("2,3,4")
                .build();
    }


    @Nested
    class AddToCartTests {
        @Test
        void addToCart_success() {
            mockFindUser();
            mockFindEquipment(equipment1);
            when(equipmentCartRepository.existsByUserIdAndEquipmentId(user.getId(), equipment1.getId())).thenReturn(false);

            equipmentService.addToCart(loginUser, List.of(equipment1.getId()));

            verify(equipmentCartRepository).save(any(EquipmentCart.class));
        }

        @Test
        void addToCart_alreadyInCart() {
            mockFindUser();
            mockFindEquipment(equipment1);
            when(equipmentCartRepository.existsByUserIdAndEquipmentId(user.getId(), equipment1.getId())).thenReturn(true);

            equipmentService.addToCart(loginUser, List.of(equipment1.getId()));

            verify(equipmentCartRepository, never()).save(any());
        }
    }

    @Nested
    class GetCartItemsTests {
        @Test
        void getCartItems_success() {
            EquipmentCart cart1 = EquipmentCart.builder().userId(user.getId()).equipmentId(equipment1.getId()).build();
            EquipmentCart cart2 = EquipmentCart.builder().userId(user.getId()).equipmentId(equipment2.getId()).build();

            when(equipmentCartRepository.findByUserId(loginUser.getId())).thenReturn(List.of(cart1, cart2));
            mockFindEquipment(equipment1);
            mockFindEquipment(equipment2);

            List<EquipmentResponse> result = equipmentService.getCartItems(loginUser);

            assertThat(result).hasSize(2);
        }

        @Test
        void getCartItems_emptyCart() {
            when(equipmentCartRepository.findByUserId(loginUser.getId())).thenReturn(Collections.emptyList());

            List<EquipmentResponse> result = equipmentService.getCartItems(loginUser);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class RequestRentalTests {
        private EquipmentRentalRequest rentalRequest;
        private LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        private LocalDateTime endDate = LocalDateTime.now().plusDays(3);

        @BeforeEach
        void rentalSetup() {
            rentalRequest = EquipmentRentalRequest.builder()
                    .equipmentIds(List.of(equipment1.getId()))
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        }

        @Test
        void requestRental_success() {
            mockFindUser();
            mockFindEquipmentForUpdate(equipment1);

            equipmentService.requestRental(loginUser, rentalRequest);

            verify(equipmentRepository).save(any());
            verify(equipmentCartRepository).deleteByUserIdAndEquipmentId(user.getId(), equipment1.getId());
        }
    }

    @Nested
    class CancelRentalRequestTests {
        @Test
        void cancelRentalRequest_success() {
            equipment1 = equipment1.toBuilder().status(Status.RENTAL_PENDING).renter(user).build();
            mockFindUser();
            mockFindEquipment(equipment1);

            equipmentService.cancelRentalRequest(loginUser, List.of(equipment1.getId()));

            verify(equipmentRepository).save(any());
        }
    }

    @Nested
    class RequestReturnTests {
        @Test
        void requestReturn_success() {
            equipment1 = equipment1.toBuilder().status(Status.IN_USE).renter(user).build();
            mockFindEquipment(equipment1);

            equipmentService.requestReturn(loginUser, List.of(equipment1.getId()));

            verify(equipmentRepository).save(any());
        }
    }

    @Nested
    class CancelReturnRequestTests {
        @Test
        void cancelReturnRequest_success() {
            equipment1 = equipment1.toBuilder().status(Status.RETURN_PENDING).renter(user).build();
            mockFindEquipment(equipment1);

            equipmentService.cancelReturnRequest(loginUser, List.of(equipment1.getId()));

            verify(equipmentRepository).save(any());
        }
    }

    private void mockFindUser() {
        when(userRepository.findById(loginUser.getId())).thenReturn(Optional.of(user));
    }

    private void mockFindEquipment(Equipment equipment) {
        when(equipmentRepository.findById(equipment.getId())).thenReturn(Optional.of(equipment));
    }

    private void mockFindEquipmentForUpdate(Equipment equipment) {
        when(equipmentRepository.findByIdForUpdate(equipment.getId())).thenReturn(Optional.of(equipment));
    }
}
