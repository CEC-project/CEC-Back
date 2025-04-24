package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.model.entity.enums.Role;
@Service
@RequiredArgsConstructor
public class AdminEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ADMIN,Role.MANAGER);
        return new AdminManagerCandidatesResponse(adminUsers);
        
    }
} 