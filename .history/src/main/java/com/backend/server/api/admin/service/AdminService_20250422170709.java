package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.AdminRentalApprovalRequest;
import com.backend.server.api.admin.dto.AdminReturnApprovalRequest;
import com.backend.server.api.admin.dto.AdminRentalResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentRentalRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentRentalRepository rentalRepository;
    private final UserRepository userRepository;

    // 장비 관련 메소드

    @Transactional(readOnly = true)
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    @Transactional
    public Equipment createEquipment(AdminEquipmentCreateRequest request) {
        // 관리자 정보 확인
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + request.getManagerId()));
        
        // 관리자가 ADMIN 역할을 가지고 있는지 확인
        if (!"ADMIN".equals(manager.getRole())) {
            throw new IllegalArgumentException("관리자 권한이 없는 사용자입니다: " + request.getManagerId());
        }

        Equipment equipment = request.toEntity(manager, null);
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public Equipment updateEquipment(Long id, AdminEquipmentCreateRequest request) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + id));
        
        // 관리자 정보 확인
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + request.getManagerId()));
        
        // 관리자가 ADMIN 역할을 가지고 있는지 확인
        if (!"ADMIN".equals(manager.getRole())) {
            throw new IllegalArgumentException("관리자 권한이 없는 사용자입니다: " + request.getManagerId());
        }

        Equipment updatedEquipment = request.toEntity(manager, existingEquipment);
        return equipmentRepository.save(updatedEquipment);
    }

    @Transactional
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + id));
        
        equipmentRepository.delete(equipment);
    }

    // 대여 요청 관련 메소드

    @Transactional(readOnly = true)
    public List<AdminRentalResponse> getRentalRequests() {
        List<EquipmentRental> rentalRequests = rentalRepository.findByStatus(RentalStatus.RENTAL_PENDING);
        return createAdminRentalResponses(rentalRequests);
    }

    @Transactional(readOnly = true)
    public List<AdminRentalResponse> getReturnRequests() {
        List<EquipmentRental> returnRequests = rentalRepository.findByStatus(RentalStatus.RETURN_PENDING);
        return createAdminRentalResponses(returnRequests);
    }

    @Transactional
    public void approveRentalRequest(Long rentalId, AdminRentalApprovalRequest request) {
        EquipmentRental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대여 요청이 존재하지 않습니다: " + rentalId));
        
        if (rental.getStatus() != RentalStatus.RENTAL_PENDING) {
            throw new IllegalStateException("대여 요청 상태가 아닙니다: " + rental.getStatus());
        }
        
        // 대여 요청 승인 처리
        rental.approveRental();
        rentalRepository.save(rental);
        
        // 장비 상태 업데이트
        Equipment equipment = equipmentRepository.findById(rental.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + rental.getEquipmentId()));
        
        equipment.setRentalStatus(RentalStatus.IN_USE);
        equipment.setRenterId(Math.toIntExact(rental.getUserId()));
        equipment.setRentalTime(rental.getRentalTime());
        equipment.setReturnTime(rental.getReturnTime());
        equipment.setAvailable(false);
        
        equipmentRepository.save(equipment);
    }

    @Transactional
    public void rejectRentalRequest(Long rentalId, AdminRentalApprovalRequest request) {
        EquipmentRental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대여 요청이 존재하지 않습니다: " + rentalId));
        
        if (rental.getStatus() != RentalStatus.RENTAL_PENDING) {
            throw new IllegalStateException("대여 요청 상태가 아닙니다: " + rental.getStatus());
        }
        
        // 대여 요청 거절 처리
        rental.rejectRental();
        rentalRepository.save(rental);
    }

    @Transactional
    public void approveReturnRequest(Long rentalId, AdminReturnApprovalRequest request, boolean isDamaged) {
        EquipmentRental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 반납 요청이 존재하지 않습니다: " + rentalId));
        
        if (rental.getStatus() != RentalStatus.RETURN_PENDING) {
            throw new IllegalStateException("반납 요청 상태가 아닙니다: " + rental.getStatus());
        }
        
        // 반납 요청 승인 처리
        rental.completeReturn();
        rentalRepository.save(rental);
        
        // 장비 상태 업데이트
        Equipment equipment = equipmentRepository.findById(rental.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + rental.getEquipmentId()));
        
        if (isDamaged) {
            equipment.setRentalStatus(RentalStatus.REPAIR);
            equipment.setAvailable(false);
        } else {
            equipment.setRentalStatus(RentalStatus.AVAILABLE);
            equipment.setAvailable(true);
        }
        
        equipment.setRenterId(null);
        equipment.setRentalTime(null);
        equipment.setReturnTime(null);
        
        equipmentRepository.save(equipment);
    }

    // 헬퍼 메소드
    private List<AdminRentalResponse> createAdminRentalResponses(List<EquipmentRental> rentals) {
        List<AdminRentalResponse> responses = new ArrayList<>();
        
        for (EquipmentRental rental : rentals) {
            Equipment equipment = equipmentRepository.findById(rental.getEquipmentId()).orElse(null);
            User user = userRepository.findById(rental.getUserId()).orElse(null);
            
            AdminRentalResponse response = new AdminRentalResponse(rental, equipment, user);
            responses.add(response);
        }
        
        return responses;
    }
} 