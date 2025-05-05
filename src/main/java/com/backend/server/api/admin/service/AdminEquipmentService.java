package com.backend.server.api.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentIdResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentIdsResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.repository.EquipmentCategoryRepository;
import com.backend.server.model.repository.EquipmentModelRepository;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentService {
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    //어드민 유저 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> users = userRepository.findByRoleIn(Role.ROLE_ADMIN);
        return users.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }

    //장비생성성
    public AdminEquipmentIdsResponse createEquipment(AdminEquipmentCreateRequest request) {
        String equipmentCategoryName = equipmentCategoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("카테고리 없음"))
            .getEnglishCode(); 
    
        String equipmentModelName = equipmentModelRepository.findById(request.getModelId())
            .orElseThrow(() -> new RuntimeException("모델 없음"))
            .getEnglishCode();
    
        String prefixCategoryCode = equipmentCategoryName.substring(0, 3);
        String prefixEquipmentModelCode = equipmentModelName.substring(0, 3);
    
        Long modelCount = equipmentRepository.countByEquipmentModel_Id(request.getModelId());
    
        List<Long> savedEquipmentIds = new ArrayList<>();
        //장비 갯수 입력받은 것맨치로 반복함
        for (int i = 1; i <= request.getQuantity(); i++) {
            String modelCountString = modelCount < 10000
                ? String.format("%04d", modelCount)
                : modelCount.toString();
    
            String serialNumber = prefixCategoryCode + prefixEquipmentModelCode + modelCountString;
            Equipment equipment = request.toEntity(serialNumber);
            Equipment saved = equipmentRepository.save(equipment);
            savedEquipmentIds.add(saved.getId());
    
            modelCount++;
        }
    
        return new AdminEquipmentIdsResponse(savedEquipmentIds);
    }
    
}
