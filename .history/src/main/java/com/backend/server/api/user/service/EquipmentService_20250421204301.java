package com.backend.server.api.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        
        // 장비가 대여 가능한지 확인
        if (equipment.getAvailable() != null && !equipment.getAvailable()) {
            throw new RuntimeException("현재 대여 불가능한 장비입니다.");
        }
        
        EquipmentRental rental = request.toEntity(userId, request.getRentalTime(), request.getReturnTime());
        // 새로운 대여 신청 생성
        equipmentRentalRepository.save(rental);

        return new RentalResponse(rental);
    }

    //다중장비대여요청
    @Transactional
    public RentalListResponse createRentRequests(RentalListRequest request) {
        // 현재 로그인한 사용자 조회
        Long userId = securityUtil.getCurrentUserId();
        
        // 대여 시간과 반납 시간 설정
        LocalDateTime rentalTime = request.getStartTime();
        LocalDateTime returnTime = request.getEndTime();
        
        // Stream을 사용하여 각 장비 ID에 대해 대여 요청 생성 및 필터링
        List<RentalResponse> successResponses = request.getEquipmentIds().stream()
            .map(equipmentId -> {
                try {
                    // 장비 조회
                    Equipment equipment = equipmentRepository.findById(equipmentId)
                            .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                    
                    // 장비가 대여 가능한지 확인
                    if (equipment.getAvailable() != null && !equipment.getAvailable()) {
                        return null; // 대여 불가능한 장비는 null 반환
                    }
                    
                    // 요청 객체 생성 및 엔티티 변환
                    RentalRequest singleRequest = new RentalRequest();
                    singleRequest.setEquipmentId(equipmentId);
                    
                    // Entity 변환 및 저장
                    EquipmentRental rental = singleRequest.toEntity(userId, rentalTime, returnTime);
                    EquipmentRental savedRental = equipmentRentalRepository.save(rental);
                    
                    // 응답 생성
                    return new RentalResponse(savedRental);
                } catch (Exception e) {
                    return null; // 예외 발생 시 null 반환
                }
            })
            .filter(response -> response != null) // null이 아닌 응답만 필터링
            .collect(Collectors.toList());
        
        // 최종 응답 생성 및 반환
        return new RentalListResponse(successResponses);
    }
}
