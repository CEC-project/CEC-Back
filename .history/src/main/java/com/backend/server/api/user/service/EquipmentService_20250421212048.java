package com.backend.server.api.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.user.dto.EquipmentListRequest;
import com.backend.server.api.user.dto.EquipmentListResponse;
import com.backend.server.api.user.dto.EquipmentResponse;
import com.backend.server.api.user.dto.RentalListRequest;
import com.backend.server.api.user.dto.RentalListResponse;
import com.backend.server.api.user.dto.RentalRequest;
import com.backend.server.api.user.dto.RentalResponse;
import com.backend.server.api.user.dto.RentalListResponse.FailedRentalInfo;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.EquipmentRentalRepository;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentRentalRepository equipmentRentalRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    //장비 목록 조회
    public EquipmentListResponse getEquipments(EquipmentListRequest request) {

        Pageable pageable = EquipmentSpecification.getPageable(request);
        //유저학년찾기
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Long grade = Long.parseLong(user.getGrade());
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, grade);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return new EquipmentListResponse(page);
    }
    
    //장비 상세 조회
    public EquipmentResponse getEquipment(Long id) {
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        // 상세 정보로 변환하여 반환
        return new EquipmentResponse(equipment);
    }
    
    //장비대여요청
    @Transactional
    public RentalResponse createRentRequest(RentalRequest request) {
        // 현재 로그인한 사용자 조회
        Long userId = securityUtil.getCurrentUserId();
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        Long equipmentId = request.getEquipmentId();
        // 장비가 대여 가능한지 확인
        if (equipment.getAvailable() != null && !equipment.getAvailable()) {
            throw new RuntimeException("현재 대여 불가능한 장비입니다.");
        }
        
        EquipmentRental rental = request.toEntity(userId, equipmentId, request.getRentalTime(), request.getReturnTime());
        // 새로운 대여 신청 생성
        equipmentRentalRepository.save(rental);

        return new RentalResponse(rental);
    }

    //다중장비대여요청
    @Transactional
    public RentalListResponse createRentRequests(RentalListRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        
        List<RentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();
        
        LocalDateTime rentalTime = request.getStartTime();
        LocalDateTime returnTime = request.getEndTime();
        

        for (Long equipmentId : request.getEquipmentIds()) {
            try {
                // 장비 조회
                Equipment equipment = equipmentRepository.findById(equipmentId)
                        .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                
                // 장비가 대여 가능한지 확인
                if (equipment.getAvailable() != null && !equipment.getAvailable()) {
                    // 대여 불가능한 장비는 실패 목록에 추가
                    failedRequests.add(new FailedRentalInfo(equipmentId, "현재 대여 불가능한 장비입니다."));
                    continue;
                }
                
                RentalRequest singleRequest = new RentalRequest();
                
                EquipmentRental rental = singleRequest.toEntity(userId, equipmentId, rentalTime, returnTime);
                EquipmentRental savedRental = equipmentRentalRepository.save(rental);
                
                RentalResponse successResponse = new RentalResponse(savedRental);
                successResponses.add(successResponse);
                
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                failedRequests.add(new FailedRentalInfo(equipmentId, e.getMessage()));
            }
        }
        return new RentalListResponse(successResponses, failedRequests);
    }

    public RentalResponse createReturnRequest(RentalRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        EquipmentRental rental = equipmentRentalRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                
        request.toEntity(userId, request.getEquipmentId(), request.getRentalTime(), request.getReturnTime());
        equipmentRentalRepository.save(rental);
        return new RentalResponse(rental);
    }
}
