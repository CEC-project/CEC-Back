package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    //장비등록할떄 관리자 리스트 뽑아오기
    @Transactional(readOnly = true)
    public List<User> getAdminUsers() {
        return userRepository.findByRole("ADMIN");
    }

    //장비등록
    @Transactional
    public Equipment createEquipment(AdminEquipmentCreateRequest request) {
        // 관리자 정보 확인
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + request.getManagerId()));
        
        // 관리자가 ADMIN 역할을 가지고 있는지 확인
        if (!"ADMIN".equals(manager.getRole())) {
            throw new IllegalArgumentException("관리자 권한이 없는 사용자입니다: " + request.getManagerId());
        }

        Equipment equipment = Equipment.builder()
                .name(request.getName())
                .category(request.getCategory())
                .modelName(request.getModelName())
                .status(request.getStatus())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .attachment(request.getAttachment())
                .managerId(request.getManagerId())
                .managerName(manager.getName())
                .rentalStatus(RentalStatus.AVAILABLE)
                .build();
  
        return equipmentRepository.save(equipment);
    }
    @Transactional(readOnly = true)
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

   
    @Transactional(readOnly = true)
    public List<Equipment> getEquipmentsByCategory(String category) {
        return equipmentRepository.findByCategory(category);
    }

 
    @Transactional
    public Equipment updateEquipment(Long id, AdminEquipmentCreateRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + id));
        
        // 관리자 정보 확인
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + request.getManagerId()));
        
        // 관리자가 ADMIN 역할을 가지고 있는지 확인
        if (!"ADMIN".equals(manager.getRole())) {
            throw new IllegalArgumentException("관리자 권한이 없는 사용자입니다: " + request.getManagerId());
        }
        
        // 기존 장비의 상태 유지 , 값이 있다면 변경
        RentalStatus rentalStatus = equipment.getRentalStatus();
        if (request.getRentalStatus() != null) {
            try {
                rentalStatus = RentalStatus.valueOf(request.getRentalStatus());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 대여 상태입니다: " + request.getRentalStatus());
            }
        }

        // 업데이트할 필드 반영
        Equipment updatedEquipment = Equipment.builder()
                .id(equipment.getId())
                .name(request.getName())
                .category(request.getCategory())
                .modelName(request.getModelName())
                .status(request.getStatus())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .attachment(request.getAttachment())
                .managerId(request.getManagerId())
                .managerName(manager.getName())
                .rentalStatus(rentalStatus)
                .rentalTime(equipment.getRentalTime())
                .returnTime(equipment.getReturnTime())
                .renterId(equipment.getRenterId())
                .build();
        
        return equipmentRepository.save(updatedEquipment);
    }


    @Transactional
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + id));
        
        equipmentRepository.delete(equipment);
    }
} 