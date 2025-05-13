package com.backend.server.api.user.equipment.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentRentalRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;
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

    //장비 하나 호버링시 이미지 보여주기
    public EquipmentResponse getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));
        
        return new EquipmentResponse(equipment);
    }
    //장비목록조회회
    public EquipmentListResponse getEquipments(LoginUser loginUser,EquipmentListRequest request) {
        // 사용자 학년 정보 조회
        Integer userGrade = userRepository.findById(loginUser.getId()).map(User::getGrade).orElse(null);

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

    //장비 대여 요청
    @Transactional
    public void requestRental(LoginUser loginUser, EquipmentRentalRequest request) {
        User user = userRepository.findById(loginUser.getId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        for (Long equipmentId : request.getEquipmentIds()) {
            Equipment equipment = equipmentRepository.findByIdForUpdate(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 장비가 대여 가능한 상태인지 확인
            if (Status.AVAILABLE != equipment.getStatus()) {
                throw new IllegalStateException("장비가 대여 불가능한 상태입니다: " + equipment.getId());
            }
            
            Equipment updatedEquipment = equipment.toBuilder()
                .status(Status.RENTAL_PENDING)
                .renterId(user.getId())
                .startRentDate(request.getStartDate())
                .endRentDate(request.getEndDate())
                .build();
            
            equipmentRepository.save(updatedEquipment);
            
            // 장바구니에서 제거
            equipmentCartRepository.deleteByUserIdAndEquipmentId(user.getId(), equipmentId);
        }
    }

    @Transactional
    public void cancelRentalRequest(LoginUser loginUser, List<Long> equipmentIds) {
        User user = userRepository.findById(loginUser.getId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 본인의 대여 요청인지 확인
            if (!user.getId().equals(equipment.getRenterId())) {
                throw new IllegalStateException("본인의 대여 요청만 취소할 수 있습니다.");
            }

            // 대여 요청 상태인지 확인
            if (Status.RENTAL_PENDING != equipment.getStatus()) {
                throw new IllegalStateException("대여 요청 상태인 경우에만 취소할 수 있습니다.");
            }

            Equipment updatedEquipment = equipment.toBuilder()
                .status(Status.AVAILABLE)
                .renterId(null)
                .startRentDate(null)
                .endRentDate(null)
                .build();   

            equipmentRepository.save(updatedEquipment);

        }
    }

    @Transactional
    public void requestReturn(LoginUser loginUser, List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 본인의 대여인지 확인
            if (!loginUser.getId().equals(equipment.getRenterId())) {
                throw new IllegalStateException("본인의 대여만 반납 요청할 수 있습니다.");
            }

            // 대여 중인 상태인지 확인
            if (!Status.IN_USE.equals(equipment.getStatus())) {
                throw new IllegalStateException("대여 중인 상태인 경우에만 반납 요청할 수 있습니다.");
            }


            Equipment updatedEquipment = equipment.toBuilder()
                .status(Status.AVAILABLE)
                .build();
            
            equipmentRepository.save(updatedEquipment);
        }
    }

    @Transactional
    public void cancelReturnRequest(LoginUser loginUser, List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다."));

            // 본인의 대여인지 확인
            if (!loginUser.getId().equals(equipment.getRenterId())) {
                throw new IllegalStateException("본인의 대여만 반납 요청 취소할 수 있습니다.");
            }

            // 반납 요청 상태인지 확인
            if (!Status.RETURN_PENDING.equals(equipment.getStatus())) {
                throw new IllegalStateException("반납 요청 상태인 경우에만 취소할 수 있습니다.");
            }

            
            Equipment updatedEquipment = equipment.toBuilder()
                .status(Status.IN_USE)
                .build();

            equipmentRepository.save(updatedEquipment);
        }
    }

    // public List<Equipment> getMyRentals(Long userId) {
    //     return equipmentRepository.findByRenterIdOrderByRequestedAtDesc(userId);
    // }
}
