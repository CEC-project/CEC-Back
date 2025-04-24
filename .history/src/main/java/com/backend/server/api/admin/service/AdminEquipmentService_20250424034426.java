package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListResponse;
import com.backend.server.api.admin.dto.AdminEquipmentResponse;
import com.backend.server.api.user.dto.EquipmentListRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.model.entity.enums.Role;
@Service
@RequiredArgsConstructor
public class AdminEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    //장비등록할떄 관리자 목록 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }

    public AdminEquipmentListResponse getEquipments(AdminEquipmentListRequest request) {
        Pageable pageable = EquipmentSpecification.getPageable(request);

        Specification<Equipment> spec = EquipmentSpecification.AdminfilterEquipments(request);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return new AdminEquipmentListResponse(page);
    }

    public AdminEquipmentResponse getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        return new AdminEquipmentResponse(equipment);
    }
    
} 