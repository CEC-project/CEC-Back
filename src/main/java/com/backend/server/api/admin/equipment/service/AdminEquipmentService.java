package com.backend.server.api.admin.equipment.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.enums.BrokenType;
import com.backend.server.model.entity.equipment.*;
import com.backend.server.model.repository.equipment.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.admin.equipment.dto.equipment.response.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentService {
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentBrokenHistoryRepository equipmentBrokenHistoryRepository;
    private final EquipmentRepairHistoryRepository equipmentRepairHistoryRepository;
    
    //어드민 유저 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
            .map(AdminManagerCandidatesResponse::new)
            .collect(Collectors.toList());
    }

    public String generateSerialNumber(AdminEquipmentSerialNumberGenerateRequest request) {

        EquipmentCategory category = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        EquipmentModel model = equipmentModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new RuntimeException("모델 없음"));

        // prefix: 카테고리 코드 3자리 + 모델 코드 3자리
        //영문코드가 3자리 이하인 경우 문자열 앞에 _ 를 붙임.
        //ex - _CA, __A
        //_CA__A250501 이런식
        String prefixCategoryCode;
        String prefixEquipmentModelCode;


        String categoryCode = category.getEnglishCode().toUpperCase();
        if (categoryCode.length() < 3) {
            prefixCategoryCode = String.format("%3s", categoryCode).replace(' ', '_');
        } else {
            prefixCategoryCode = categoryCode.substring(0, 3);
        }

        String modelCode = model.getEnglishCode().toUpperCase();
        if (modelCode.length() < 3) {
            prefixEquipmentModelCode = String.format("%3s", modelCode).replace(' ', '_');
        } else {
            prefixEquipmentModelCode = modelCode.substring(0, 3);
        }
        String prefix = prefixCategoryCode + prefixEquipmentModelCode; // e.g., CAMSON

        // suffix: yyMM + 2자리 일련번호
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM")); // e.g., 2405
        String serialPrefix = prefix + datePrefix; // e.g., CAMSON2405

        long count = equipmentRepository.countBySerialNumberStartingWith(serialPrefix);
        String sequence = String.format("%02d", count + 1); // 첫 번째 생성될 순번

        return serialPrefix + sequence; // e.g., CAMSON240501
    }
    //장비생성
    public List<Long> createEquipment(AdminEquipmentCreateRequest request) {
        EquipmentCategory category = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        EquipmentModel model = equipmentModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new RuntimeException("모델 없음"));


        String prefixCategoryCode = category.getEnglishCode().substring(0, 3).toUpperCase(); // ex: CAM
        String prefixModelCode = model.getEnglishCode().substring(0, 3).toUpperCase();       // ex: SON
        String prefix = prefixCategoryCode + prefixModelCode;                                // ex: CAMSON

        // 날짜 구성: yyMM
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));     // ex: 2405
        String serialPrefix = prefix + datePrefix;                                            // ex: CAMSON2405

        // 기존 시리얼 넘버 개수 파악 (해당 월 기준)
        long count = equipmentRepository.countBySerialNumberStartingWith(serialPrefix);

        List<Long> savedEquipmentIds = new ArrayList<>();

        for (int i = 1; i <= request.getQuantity(); i++) {
            String sequence = String.format("%02d", count + i);                               // 01 ~ 99
            String serialNumber = serialPrefix + sequence;                                    // ex: CAMSON240501

            Equipment equipment = request.toEntity(category, model, serialNumber);
            Equipment saved = equipmentRepository.save(equipment);
            savedEquipmentIds.add(saved.getId());
        }

        return savedEquipmentIds;
    }


    // 장비 업데이트
    public Long updateEquipment(Long id, AdminEquipmentCreateRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));

        // toBuilder가 맞는거같아요
        Equipment updated = equipment.toBuilder()
            .imageUrl(request.getImageUrl())
            .managerId(request.getManagerId())
            .description(request.getDescription())
            .restrictionGrade(request.getRestrictionGrade())
            .build();

        updated = equipmentRepository.save(updated);

        return updated.getId();
    }

    // 장비 삭제
    public Long deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
        return id;
    }


    //장비 리스트 조회
    public AdminEquipmentListResponse getEquipments(AdminEquipmentListRequest request) {
        Pageable pageable = EquipmentSpecification.getPageable(request);

        Specification<Equipment> spec = EquipmentSpecification.adminFilterEquipments(request);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        List<AdminEquipmentResponse> responses = page.getContent().stream()
            .map(AdminEquipmentResponse::new)
            .toList();
        return new AdminEquipmentListResponse(responses, page);
    }

    //장비 단일 조회
    public AdminEquipmentResponse getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));

        return new AdminEquipmentResponse(equipment);
    }
    
    //장비 강제 상태 변경경
    @Transactional
    public Long updateEquipmentStatus(Long equipmentId, AdminEquipmentStatusUpdateRequest request) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
            .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        Equipment updated = equipment.toBuilder()
            .status(request.getStatus())
            .build();
        
        equipmentRepository.save(updated);
        return equipmentId;
    }


    //장비 파손 수리 등록
    @Transactional
    public List<Long> changeStatus(AdminEquipmentBrokenOrRepairRequest request, LoginUser loginUser) {
        BiFunction<Long, String, Long> operator;

        switch (request.getStatus()) {
            case BROKEN -> operator = (id, detail) -> changeToBrokenStatus(id, detail, loginUser);
            case REPAIR -> operator = (id, detail) -> changeToRepairStatus(id, detail, loginUser);
            default -> throw new IllegalArgumentException("지원하지 않는 상태입니다: " + request.getStatus());
        }

        for (Long equipmentId : request.getEquipmentIds()) {
            operator.apply(equipmentId, request.getDetail());
        }

        return request.getEquipmentIds();
    }

    //장비 상태 파손 처리
    @Transactional
    public Long changeToBrokenStatus(Long equipmentId, String detail, LoginUser loginUser){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(()->new IllegalArgumentException("관리자를 찾을 수 없습니다"));

        EquipmentBrokenHistory history = EquipmentBrokenHistory.builder()
                .equipment(equipment)
                .brokenBy(user)
                .brokenType(BrokenType.ADMIN_DAMAGED)
                .brokenDetail(detail)
                .build();

        equipmentBrokenHistoryRepository.save(history);

        equipment = equipment.toBuilder()
                .status(Status.BROKEN)
                .renter(null)
                .startRentDate(null)
                .endRentDate(null)
                .build();

        equipmentRepository.save(equipment);
        return  equipmentId;
    }
    //장비 상태 수리 처리
    @Transactional
    public Long changeToRepairStatus(Long equipmentId, String detail, LoginUser loginUser) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("장비를 찾을 수 없습니다: " + equipmentId));

        if (equipment.getStatus() != Status.BROKEN) {
            throw new IllegalArgumentException("고장(BROKEN) 상태의 장비만 수리할 수 있습니다.");
        }

        // 최근 고장 이력 찾기
        EquipmentBrokenHistory brokenHistory = equipmentBrokenHistoryRepository
                .findTopByEquipmentOrderByCreatedAtDesc(equipment)
                .orElseThrow(() -> new IllegalStateException("고장 이력이 존재하지 않습니다."));

        // 수리 이력 기록
        EquipmentRepairHistory repairHistory = EquipmentRepairHistory.builder()
                .equipment(equipment)
                .equipmentBrokenHistory(brokenHistory)
                .repairDetail(detail)
                .build();

        equipmentRepairHistoryRepository.save(repairHistory);

        // 장비 상태 변경
        Equipment updated = equipment.toBuilder()
                .status(Status.AVAILABLE)
                .build();

        equipmentRepository.save(updated);

        return equipmentId;
    }


    
    
}
