package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentListRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentListResponse;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentResponse;
import com.backend.server.api.user.dto.equipment.EquipmentListRequest;
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
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.repository.EquipmentRentalRepository;



@Service
@RequiredArgsConstructor
public class AdminEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final EquipmentRentalRepository equipmentRentalRepository;

    //장비등록할떄 관리자 목록 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }
    //모든장비 조회 / 필터링
    public AdminEquipmentListResponse getEquipments(AdminEquipmentListRequest request) {
        Pageable pageable = EquipmentSpecification.getPageable(request);

        Specification<Equipment> spec = EquipmentSpecification.AdminfilterEquipments(request);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return new AdminEquipmentListResponse(page);
    }
    //장비 상세 조회
    public AdminEquipmentResponse getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        return new AdminEquipmentResponse(equipment);
    }

    //장비 등록
    public void createEquipment(AdminEquipmentCreateRequest request) {
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));
        Equipment equipment = request.toEntity(manager, null);
        equipmentRepository.save(equipment);
    }

    //장비 수정
    public void updateEquipment(Long id, AdminEquipmentCreateRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));
        equipment = request.toEntity(manager, equipment);
        equipmentRepository.save(equipment);
    }

    //장비 삭제
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    //장비 대여/반납 요청 목록 조회 (status를 다르게해서 대여요청 목록과 반납요청 목록 둘다 조회 가능)
    public AdminEquipmentRentalRequestListResponse getRentalRequests(AdminEquipmentRentalRequestListRequest request) {
        Pageable pageable = EquipmentSpecification.getRentalRequestPageable(request);
        Specification<EquipmentRental> spec = EquipmentSpecification.filterRentalRequests(request);
    
        // 1. 대여 내역 페이지네이션
        Page<EquipmentRental> page = equipmentRentalRepository.findAll(spec, pageable);
    
        // 2. 유저랑 장비 ID 가져옴
        List<Long> userIds = page.getContent().stream()
            .map(EquipmentRental::getUserId)
            .distinct()
            .toList();
    
        List<Long> equipmentIds = page.getContent().stream()
            .map(EquipmentRental::getEquipmentId)
            .distinct()
            .toList();
    
        // 3. 유저와 장비를 한 번에 조회하고 맵으로 전환ㅇ..... 이거 너무 어렵다
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));
            //User::getId, Function.identity() 이거는
            //유저 아이디가 키값, 그리고 그 아이디에 맞는 유저 정보. 유저 객체가 벨류임.
    
        Map<Long, Equipment> equipmentMap = equipmentRepository.findAllById(equipmentIds).stream()
            .collect(Collectors.toMap(Equipment::getId, Function.identity()));
    
        // 4.위 과정 조립
        return new AdminEquipmentRentalRequestListResponse(page, userMap, equipmentMap);
    }

    //장비 대여 요청 다중 승인
    public void approveRentalRequests(List<Long> ids) {
        List<EquipmentRental> rentals = equipmentRentalRepository.findAllById(ids);
        rentals.forEach(EquipmentRental::approveRental); // 엔티티에 정의된 메소드 사용
        equipmentRentalRepository.saveAll(rentals);
    }

    //장비 대여 요청 다중 거절
    public void rejectRentalRequests(List<Long> ids) {
        List<EquipmentRental> rentals = equipmentRentalRepository.findAllById(ids);
        rentals.forEach(EquipmentRental::rejectRental); // 엔티티에 정의된 메소드 사용
        equipmentRentalRepository.saveAll(rentals);
    }


    //장비 반납 다중승인(정상 반납)
    public void approveReturnRequestsNormal(List<Long> ids) {
        List<EquipmentRental> returns = equipmentRentalRepository.findAllById(ids);
        returns.forEach(EquipmentRental::completeReturn); // 엔티티에 정의된 메소드 사용
        equipmentRentalRepository.saveAll(returns);
    }

    //장비 반납 다중승인(파손 반납)
    public void approveReturnDamegedRequestsNormal(List<Long> ids) {
        List<EquipmentRental> returns = equipmentRentalRepository.findAllById(ids);
        returns.forEach(EquipmentRental::completeReturnDamaged); // 엔티티에 정의된 메소드 사용
        equipmentRentalRepository.saveAll(returns);
    }
    
} 