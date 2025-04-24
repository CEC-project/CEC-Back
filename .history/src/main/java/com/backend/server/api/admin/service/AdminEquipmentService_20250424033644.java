package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListRequest;
import com.backend.server.api.admin.dto.AdminEquipmentListResponse;
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
    private final EquipmentSpecification equipmentSpecification;

    //장비등록할떄 관리자 목록 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }

    public AdminEquipmentListResponse getAllEquipments(AdminEquipmentListRequest request) {
        // Convert AdminEquipmentListRequest to EquipmentListRequest
        EquipmentListRequest equipmentRequest = new EquipmentListRequest();
        equipmentRequest.setPage(request.getPage());
        equipmentRequest.setSize(request.getSize());
        equipmentRequest.setCategory(request.getCategory());
        equipmentRequest.setStatus(request.getStatus());
        equipmentRequest.setAvailable(request.getAvailable());
        equipmentRequest.setSearchKeyword(request.getSearchKeyword());
        equipmentRequest.setSearchType(request.getSearchType());
        equipmentRequest.setSortBy(request.getSortBy());
        equipmentRequest.setSortDirection(request.getSortDirection());
        equipmentRequest.setFavoriteOnly(request.getFavoriteOnly());
        
        Pageable pageable = EquipmentSpecification.getPageable(equipmentRequest);
        Specification<Equipment> specification = EquipmentSpecification.AdminfilterEquipments(equipmentRequest);
        
        Page<Equipment> equipments = equipmentRepository.findAll(specification, pageable);
        return new AdminEquipmentListResponse(equipments);
    }
    
} 