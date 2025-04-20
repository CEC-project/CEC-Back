package com.backend.server.api.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.backend.server.api.user.dto.EquipmentListRequest;
import com.backend.server.api.user.dto.EquipmentListResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public EquipmentListResponse getEquipments(EquipmentListRequest request) {
        // 페이징 및 정렬 정보 설정
        Pageable pageable = EquipmentSpecification.getPageable(request);
        
        // 필터링 설정
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, userGrade);
        
        // 조회 실행
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        // 응답 생성
        return new EquipmentListResponse(page);
    }
    
    public EquipmentListResponse getUserEquipments(EquipmentListRequest request, Long userId) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 페이징 및 정렬 정보 설정
        Pageable pageable = EquipmentSpecification.getPageable(request);
        
        // 사용자 grade가 String 타입이므로 정수로 변환 시도
        Integer userGrade = null;
        try {
            if (user.getGrade() != null && !user.getGrade().isEmpty()) {
                userGrade = Integer.parseInt(user.getGrade());
            }
        } catch (NumberFormatException e) {
            // Grade가 숫자가 아닌 형식인 경우 무시하고 null로 설정
        }
        
        // 사용자 필터링 설정 (학년 제한 등 적용)
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments2(request, userGrade);
        
        // 조회 실행
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        // 응답 생성
        return new EquipmentListResponse(page);
    }
}
