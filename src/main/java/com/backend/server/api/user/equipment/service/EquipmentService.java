package com.backend.server.api.user.equipment.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.api.user.equipment.dto.equipment.*;
import com.backend.server.model.entity.enums.EquipmentAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.EquipmentCart;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import com.backend.server.model.repository.equipment.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.EquipmentCartRepository;

import lombok.RequiredArgsConstructor;

import static com.backend.server.model.entity.enums.EquipmentAction.RENT_REQUEST;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final EquipmentCartRepository equipmentCartRepository;
    private final CommonNotificationService notificationService;

    //장비 하나 호버링시 이미지 보여주기
    public EquipmentResponse getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));
        
        return new EquipmentResponse(equipment);
    }
    //장비목록조회회
    public EquipmentListResponse getEquipments(LoginUser loginUser, EquipmentListRequest request) {
        // 사용자 학년 정보 조회
        Integer userGrade = userRepository.findById(loginUser.getId()).map(User::getGrade).orElse(null);

        Pageable pageable = EquipmentSpecification.getPageable(request);
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, userGrade);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        List<EquipmentResponse> responses = page.getContent().stream()
            .map(EquipmentResponse::new)
            .toList();
        return new EquipmentListResponse(responses, page);
    }

    //장비 장바구니 추가
    @Transactional
    public void addToCart(LoginUser loginUser, List<Long> equipmentIds) {
        User user = userRepository.findById(loginUser.getId())
                         .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 이미 장바구니에 있는지 확인
            if (equipmentCartRepository.existsByUserIdAndEquipmentId(user.getId(), equipmentId)) {
                continue;
            }

            // 장비가 대여 가능한 상태인지 확인
            if (Status.AVAILABLE != equipment.getStatus()) {
                throw new IllegalStateException("장비가 대여 불가능한 상태입니다: " + equipment.getId());
            }
            Integer userGrade = user.getGrade();
            if(equipment.getRestrictionGrade().contains(userGrade.toString())) {
                throw new IllegalStateException("장비 대여 제한 학년에 걸려요");
            }

            EquipmentCart cart = EquipmentCart.builder()
                .userId(user.getId())
                .equipmentId(equipment.getId())
                .build();
            
            equipmentCartRepository.save(cart);
        }
    }

    //장비 장바구니 조회
    public List<EquipmentResponse> getCartItems(LoginUser loginUser) {
        List<EquipmentCart> cartItems = equipmentCartRepository.findByUserId(loginUser.getId());
        
        return cartItems.stream()
            .map(cart -> {
                Equipment equipment = equipmentRepository.findById(cart.getEquipmentId())
                    .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));
                return new EquipmentResponse(equipment);
            })
            .toList();
    }


    @Transactional
    public void handleUserAction(LoginUser loginUser, EquipmentActionRequest request) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        BiConsumer<Long, EquipmentActionRequest> operator = switch (request.getAction()) {
            case RENT_REQUEST -> (id, req) -> handleRentRequest(user, loginUser, id, req);
            case RENT_CANCEL -> (id, req) -> handleRentCancel(user, id);
            case RETURN_REQUEST -> (id, req) -> handleReturnRequest(user, id);
            case RETURN_CANCEL -> (id, req) -> handleReturnCancel(user, id);
        };

        for (Long equipmentId : request.getEquipmentIds()) {
            operator.accept(equipmentId, request);
        }
    }

    //대여요청
    private void handleRentRequest(User user, LoginUser loginUser, Long equipmentId, EquipmentActionRequest request) {
        Equipment equipment = equipmentRepository.findByIdForUpdate(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

        if (equipment.getStatus() != Status.AVAILABLE) {
            throw new IllegalStateException("장비가 대여 불가능한 상태입니다.");
        }

        String restriction = equipment.getRestrictionGrade();
        if (restriction != null) {
            List<String> restrictedGrades = List.of(restriction.split(","));
            if (restrictedGrades.contains(String.valueOf(loginUser.getGrade()))) {
                throw new IllegalStateException("대여 제한 학년입니다.");
            }
        }

        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("대여 요청은 시작일과 종료일이 필요합니다.");
        }

        Equipment updated = equipment.toBuilder()
                .status(Status.RENTAL_PENDING)
                .renter(user)
                .startRentDate(request.getStartDate())
                .endRentDate(request.getEndDate())
                .build();

        equipmentRepository.save(updated);
        equipmentCartRepository.deleteByUserIdAndEquipmentId(user.getId(), equipmentId);

        notificationService.createNotificationToAdmins(CommonNotificationDto.builder()
                .category("장비")
                .title("장비 대여 요청이 있습니다.")
                .message(loginUser.getName() + "님의 장비 대여 요청이 있습니다.")
                .link("/approval/" + equipmentId)
                .build());
    }
    private void handleRentCancel(User user, Long equipmentId) {
        Equipment equipment = equipmentRepository.findByIdForUpdate(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

        validateOwnership(user, equipment);

        if (equipment.getStatus() != Status.RENTAL_PENDING) {
            throw new IllegalStateException("대여 요청 상태만 취소할 수 있습니다.");
        }

        Equipment updated = equipment.toBuilder()
                .status(Status.AVAILABLE)
                .renter(null)
                .startRentDate(null)
                .endRentDate(null)
                .build();

        equipmentRepository.save(updated);
    }

    private void handleReturnRequest(User user, Long equipmentId) {
        Equipment equipment = equipmentRepository.findByIdForUpdate(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

        validateOwnership(user, equipment);

        if (equipment.getStatus() != Status.IN_USE) {
            throw new IllegalStateException("대여 중인 장비만 반납 요청할 수 있습니다.");
        }

        Equipment updated = equipment.toBuilder()
                .status(Status.RETURN_PENDING)
                .build();

        equipmentRepository.save(updated);
    }

    private void handleReturnCancel(User user, Long equipmentId) {
        Equipment equipment = equipmentRepository.findByIdForUpdate(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

        validateOwnership(user, equipment);

        if (equipment.getStatus() != Status.RETURN_PENDING) {
            throw new IllegalStateException("반납 요청 상태만 취소할 수 있습니다.");
        }

        Equipment updated = equipment.toBuilder()
                .status(Status.IN_USE)
                .build();

        equipmentRepository.save(updated);
    }

    private void validateOwnership(User user, Equipment equipment) {
        if (!user.getId().equals(equipment.getRenter().getId())) {
            throw new IllegalStateException("본인의 장비만 처리할 수 있습니다.");
        }
    }
}
