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
import com.backend.server.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
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
    
}
