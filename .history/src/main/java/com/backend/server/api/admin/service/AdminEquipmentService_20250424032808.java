package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.converters.models.Pageable;
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
    private final EquipmentSpecification equipmentSpecification;

    //장비등록할떄 관리자 목록 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }

    public AdminEquipmentListResponse getAllEquipments(AdminEquipmentListRequest request) {
        Pageable pageable = EquipmentSpecification.getPageable(request);

        userRepository.findAll(specification)
        return new AdminEquipmentListResponse(equipments);
    }
    
} 