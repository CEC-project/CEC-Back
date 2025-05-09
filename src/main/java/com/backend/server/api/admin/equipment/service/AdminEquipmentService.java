package com.backend.server.api.admin.equipment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.backend.server.api.admin.equipment.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentIdResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentIdsResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.repository.EquipmentCategoryRepository;
import com.backend.server.model.repository.EquipmentModelRepository;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
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
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }

    //장비생성
    public AdminEquipmentIdsResponse createEquipment(AdminEquipmentCreateRequest request) {
        String equipmentCategoryName = equipmentCategoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("카테고리 없음"))
            .getEnglishCode(); 
    
        String equipmentModelName = equipmentModelRepository.findById(request.getModelId())
            .orElseThrow(() -> new RuntimeException("모델 없음"))
            .getEnglishCode();
    
        String prefixCategoryCode = equipmentCategoryName.substring(0, 3);
        String prefixEquipmentModelCode = equipmentModelName.substring(0, 3);
    
        Long modelCount = equipmentRepository.countByModelId(request.getModelId());
    
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

    // 장비 업데이트
    public AdminEquipmentIdResponse updateEquipment(Long id, AdminEquipmentCreateRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));

        // toBuilder가 맞는거같아요
        Equipment updated = equipment.toBuilder()
            .imageUrl(request.getImageUrl())
            .managerId(request.getManagerId())
            .description(request.getDescription())
            .restrictionGrade(request.getRestrictionGrade())
            .build();

        equipmentRepository.save(updated); 

        return new AdminEquipmentIdResponse(updated.getId());
    }

    // 장비 삭제
    public AdminEquipmentIdResponse deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
        return new AdminEquipmentIdResponse(id);
    }

    //장비 리스트 어드민 조회
    public AdminEquipmentListResponse getEquipments(AdminEquipmentListRequest request) {
        Pageable pageable = EquipmentSpecification.getPageable(request);

        Specification<Equipment> spec = EquipmentSpecification.adminFilterEquipments(request);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        List<AdminEquipmentResponse> responses = page.getContent().stream()
            .map(equipment -> {
                String modelName = equipmentModelRepository.findById(equipment.getModelId())
                    .map(EquipmentModel::getName)
                    .orElse("장비 모델 분류가 존재하지 않습니다");
                String renterName = equipmentRepository.findByRenterId(equipment.getRenterId())
                    .map(User::getName)
                    .orElse("대여자 이름 없음");
                return new AdminEquipmentResponse(equipment, modelName, renterName);
            })
            .toList();
        return new AdminEquipmentListResponse(responses, page);
    }

    //장비 단일 조회
    public AdminEquipmentResponse getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        String modelName = equipmentModelRepository.findById(equipment.getModelId())
            .map(EquipmentModel::getName)
            .orElse("장비 모델 분류가 존재하지 않습니다");
        String renterName = equipmentRepository.findByRenterId(equipment.getRenterId())
            .map(User::getName)
            .orElse("대여자 이름 없음");
        return new AdminEquipmentResponse(equipment, modelName, renterName);
    }
}
