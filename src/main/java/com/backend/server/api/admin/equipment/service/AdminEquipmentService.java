package com.backend.server.api.admin.equipment.service;

import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.server.api.admin.equipment.dto.equipment.request.*;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.EquipmentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.admin.equipment.dto.equipment.response.AdminManagerCandidatesResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import com.backend.server.model.repository.equipment.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEquipmentService {
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final CommonNotificationService notificationService;
    
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

    //장비 상태 변경 다중
    @Transactional
    public List<Long> updateMultipleEquipmentStatus(AdminEquipmentStatusMultipleUpdateRequest request) {
        try {
            equipmentRepository.bulkUpdateStatus(request.getStatus().name(), request.getEquipmentIds());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "장비 상태 일괄 업데이트에 실패했습니다. 대상 ID: " + request.getEquipmentIds(),
                    e
            );        }
        return request.getEquipmentIds();
    }
    
    //대여 요청 다중 승인
    @Transactional
    public void approveRentalRequests(List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (equipment.getStatus() != Status.RENTAL_PENDING) {
                throw new IllegalStateException("대여 요청 상태인 장비만 승인할 수 있습니다. ID: " + equipmentId);
            }

            // 상태 업데이트
            Equipment updated = equipment.toBuilder()
                    .status(Status.IN_USE)
                    .build();

            equipmentRepository.save(updated);

            // 대여자 정보 추출
            Long userId = equipment.getRenter().getId(); // 또는 getUser().getId()

            // 알림 전송
            CommonNotificationDto dto = CommonNotificationDto.builder()
                    .category("장비 대여 승인")
                    .title("장비 대여가 승인되었습니다.")
                    .message("요청하신 장비 [" + equipment.getEquipmentModel().getName() + "]의 대여가 승인되었습니다.")
                    .link("/equipment/" + equipmentId)
                    .build();

            notificationService.createNotification(dto, userId);
        }
    }


    //대여요청 거절절
    @Transactional
    public void rejectRentalRequests(List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (equipment.getStatus() != Status.RENTAL_PENDING) {
                throw new IllegalStateException("대여 요청 상태인 장비만 거절할 수 있습니다. ID: " + equipmentId);
            }

            Long userId = equipment.getRenter().getId(); // null 설정 전
            Equipment updated = equipment.toBuilder()
                    .status(Status.AVAILABLE)
                    .renter(null)
                    .startRentDate(null)
                    .endRentDate(null)
                    .build();

            equipmentRepository.save(updated);

            CommonNotificationDto dto = CommonNotificationDto.builder()
                    .category("장비 대여 거절")
                    .title("장비 대여가 거절되었습니다.")
                    .message("요청하신 장비 [" + equipment.getEquipmentModel().getName() + "]의 대여가 거절되었습니다.")
                    .link(null)
                    .build();

            notificationService.createNotification(dto, userId);
        }
    }

    @Transactional
    public void processReturnRequests(List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (Status.RETURN_PENDING != equipment.getStatus() && Status.IN_USE != equipment.getStatus()) {
                throw new IllegalStateException("반납 요청 상태 또는 대여중인 장비만 반납 처리할 수 있습니다. ID: " + equipmentId);
            }

            Equipment updated = equipment.toBuilder()
                    .status(Status.AVAILABLE)
                    .renter(null)
                    .startRentDate(null)
                    .endRentDate(null)
                    .build();

            equipmentRepository.save(updated);
        }
    }

    @Transactional
    public void markEquipmentsAsBroken(List<Long> equipmentIds, String description) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            Long currentBrokenCount = equipment.getBrokenCount();

            Equipment updated = equipment.toBuilder()
                    .status(Status.BROKEN)
                    .description(description != null ?
                            equipment.getDescription() + "\n[" + LocalDateTime.now() + "] 고장/파손: " + description :
                            equipment.getDescription())
                    .brokenCount(currentBrokenCount != null ? currentBrokenCount + 1L : 1L)
                    .build();

            equipmentRepository.save(updated);
        }
    }

    @Transactional
    public void repairEquipments(List<Long> equipmentIds, String repairNote) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (Status.BROKEN != equipment.getStatus()) {
                throw new IllegalStateException("고장/파손 상태인 장비만 복구할 수 있습니다. ID: " + equipmentId);
            }

            Long currentRepairCount = equipment.getRepairCount();

            Equipment updated = equipment.toBuilder()
                    .status(Status.AVAILABLE)
                    .description(repairNote != null ?
                            equipment.getDescription() + "\n[" + LocalDateTime.now() + "] 복구: " + repairNote :
                            equipment.getDescription())
                    .repairCount(currentRepairCount != null ? currentRepairCount + 1L : 1L)
                    .build();

            equipmentRepository.save(updated);
        }
    }

    @Transactional
    public void extendRentalPeriods(List<Long> equipmentIds, LocalDateTime newEndDate) {
        if (newEndDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("연장 날짜는 현재 시간보다 이후여야 합니다.");
        }

        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (Status.IN_USE != equipment.getStatus()) {
                throw new IllegalStateException("대여 중인 장비만 기간을 연장할 수 있습니다. ID: " + equipmentId);
            }

            Equipment updated = equipment.toBuilder()
                    .endRentDate(newEndDate)
                    .build();

            equipmentRepository.save(updated);
        }
    }

    @Transactional
    public void forceReturnEquipments(List<Long> equipmentIds) {
        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (Status.IN_USE != equipment.getStatus()) {
                throw new IllegalStateException("대여 중인 장비만 강제 회수할 수 있습니다. ID: " + equipmentId);
            }

            Equipment updated = equipment.toBuilder()
                    .status(Status.AVAILABLE)
                    .renter(null)
                    .startRentDate(null)
                    .endRentDate(null)
                    .build();

            equipmentRepository.save(updated);
        }
    }


    /**
     * 대여 중인 장비 일괄 조회
     */
    public AdminEquipmentListResponse getRentedEquipments() {
        List<Equipment> rentedEquipments = equipmentRepository.findByStatus(Status.IN_USE);
        
        List<AdminEquipmentResponse> responses = rentedEquipments.stream()
            .map(AdminEquipmentResponse::new)
            .toList();
        
        return new AdminEquipmentListResponse(responses, rentedEquipments.size());
    }
    
    
}
