package com.backend.server.api.user.equipment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.equipment.dto.EquipmentListRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.EquipmentCart;
import com.backend.server.model.repository.EquipmentModelRepository;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.EquipmentCartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final UserRepository userRepository;
    private final EquipmentCartRepository equipmentCartRepository;

    public EquipmentListResponse getEquipments(EquipmentListRequest request) {
        // 사용자 학년 정보 조회
        Integer userGrade = userRepository.findById(request.getUserId()).map(User::getGrade).orElse(null);

        Pageable pageable = EquipmentSpecification.getPageable(request);
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, userGrade);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        List<EquipmentResponse> responses = page.getContent().stream()
            .map(equipment -> {
                String modelName = equipmentModelRepository.findById(equipment.getModelId())
                    .map(EquipmentModel::getName)
                    .orElse("장비 모델 분류가 존재하지 않습니다");
                String renterName = equipmentRepository.findByRenterId(equipment.getRenterId())
                    .map(User::getName)
                    .orElse("대여자 이름 없음");
                LocalDateTime startRentDate = equipment.getStartRentDate();
                LocalDateTime endRentDate = equipment.getEndRentDate();
                
                return new EquipmentResponse(equipment, modelName, renterName, startRentDate, endRentDate);
            })
            .toList();
        return new EquipmentListResponse(responses, page);
    }

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
            if (!"AVAILABLE".equals(equipment.getStatus())) {
                throw new IllegalStateException("장비가 대여 불가능한 상태입니다");
            }
            Integer userGrade = user.getGrade();
            if(equipment.getRestrictionGrade().contains(userGrade.toString())) {
                throw new IllegalStateException("장비 대여 제한 학년에 걸려요");
            }

            EquipmentCart cart = new EquipmentCart();
            cart.setUser(user);
            cart.setEquipment(equipment);
            cart.setCreatedAt(LocalDateTime.now());
            
            equipmentCartRepository.save(cart);
        }
    }

    public List<EquipmentResponse> getCartItems(Long userId) {
        List<EquipmentCart> cartItems = equipmentCartRepository.findByUserId(userId);
        
        return cartItems.stream()
            .map(cart -> {
                Equipment equipment = cart.getEquipment();
                String modelName = equipmentModelRepository.findById(equipment.getModelId())
                    .map(EquipmentModel::getName)
                    .orElse("장비 모델 분류가 존재하지 않습니다");
                String renterName = equipmentRepository.findByRenterId(equipment.getRenterId())
                    .map(User::getName)
                    .orElse("대여자 이름 없음");
                
                return new EquipmentResponse(
                    equipment,
                    modelName,
                    renterName,
                    equipment.getStartRentDate(),
                    equipment.getEndRentDate()
                );
            })
            .toList();
    }

    @Transactional
    public void requestRental(Long userId, List<Long> equipmentIds, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 장비가 대여 가능한 상태인지 확인
            if (!"AVAILABLE".equals(equipment.getState())) {
                throw new IllegalStateException("장비가 대여 불가능한 상태입니다: " + equipment.getName());
            }

            equipment.setState("REQUESTED");
            equipment.setRenterId(userId);
            equipment.setStartRentDate(startDate);
            equipment.setEndRentDate(endDate);
            equipment.setRequestedAt(LocalDateTime.now());
            
            equipmentRepository.save(equipment);
            
            // 장바구니에서 제거
            equipmentCartRepository.deleteByUserIdAndEquipmentId(userId, equipmentId);
        }
    }

    @Transactional
    public void cancelRentalRequest(Long userId, List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 본인의 대여 요청인지 확인
            if (!userId.equals(equipment.getRenterId())) {
                throw new IllegalStateException("본인의 대여 요청만 취소할 수 있습니다.");
            }

            // 대여 요청 상태인지 확인
            if (!"REQUESTED".equals(equipment.getState())) {
                throw new IllegalStateException("대여 요청 상태인 경우에만 취소할 수 있습니다.");
            }

            equipment.setState("AVAILABLE");
            equipment.setRenterId(null);
            equipment.setStartRentDate(null);
            equipment.setEndRentDate(null);
            equipment.setRequestedAt(null);
            
            equipmentRepository.save(equipment);
        }
    }

    @Transactional
    public void requestReturn(Long userId, List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 본인의 대여인지 확인
            if (!userId.equals(equipment.getRenterId())) {
                throw new IllegalStateException("본인의 대여만 반납 요청할 수 있습니다.");
            }

            // 대여 중인 상태인지 확인
            if (!"RENTED".equals(equipment.getState())) {
                throw new IllegalStateException("대여 중인 상태인 경우에만 반납 요청할 수 있습니다.");
            }

            equipment.setState("RETURN_REQUESTED");
            equipment.setReturnRequestedAt(LocalDateTime.now());
            
            equipmentRepository.save(equipment);
        }
    }

    @Transactional
    public void cancelReturnRequest(Long userId, List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 본인의 대여인지 확인
            if (!userId.equals(equipment.getRenterId())) {
                throw new IllegalStateException("본인의 대여만 반납 요청 취소할 수 있습니다.");
            }

            // 반납 요청 상태인지 확인
            if (!"RETURN_REQUESTED".equals(equipment.getState())) {
                throw new IllegalStateException("반납 요청 상태인 경우에만 취소할 수 있습니다.");
            }

            equipment.setState("RENTED");
            equipment.setReturnRequestedAt(null);
            
            equipmentRepository.save(equipment);
        }
    }

    public List<Equipment> getMyRentals(Long userId) {
        return equipmentRepository.findByRenterIdOrderByRequestedAtDesc(userId);
    }
}
