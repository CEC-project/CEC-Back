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

        Equipment equipment = request.toEntity(manager, null);
        return equipmentRepository.save(equipment);
    }

    //장비전체조회
    @Transactional(readOnly = true)
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    //개별상세조회
    @Transactional(readOnly = true)
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

   //카테고리로 검색하기
    @Transactional(readOnly = true)
    public List<Equipment> getEquipmentsByCategory(String category) {
        return equipmentRepository.findByCategory(category);
    }

    //업데이트
    @Transactional
    public Equipment updateEquipment(Long id, AdminEquipmentCreateRequest request) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + id));
        
        // 관리자 정보 확인
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다: " + request.getManagerId()));
        
        // 관리자가 ADMIN 역할을 가지고 있는지 확인
        if (!"ADMIN".equals(manager.getRole())) {
            throw new IllegalArgumentException("관리자 권한이 없는 사용자입니다: " + request.getManagerId());
        }

        Equipment updatedEquipment = request.toEntity(manager, existingEquipment);
        return equipmentRepository.save(updatedEquipment);
    }

    //지우기
    @Transactional
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장비가 존재하지 않습니다: " + id));
        
        equipmentRepository.delete(equipment);
    }
} 