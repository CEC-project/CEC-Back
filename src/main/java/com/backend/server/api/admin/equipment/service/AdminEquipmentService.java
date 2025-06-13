package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentBrokenOrRepairRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentCreateRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentSerialNumberGenerateRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentStatusUpdateRequest;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentListResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminEquipmentResponse;
import com.backend.server.api.admin.equipment.dto.equipment.response.AdminManagerCandidatesResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.BrokenRepairHistoryRepository;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import com.backend.server.model.repository.equipment.EquipmentSpecification;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEquipmentService {
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final BrokenRepairHistoryRepository brokenRepairHistoryRepository;


    //어드민 유저 조회
    public List<AdminManagerCandidatesResponse> getAdminUsers() {
        List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
        return adminUsers.stream()
                .map(AdminManagerCandidatesResponse::new)
                .collect(Collectors.toList());
    }

    private String generateSerialPrefix(EquipmentCategory category, EquipmentModel model) {
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

        // 모델 그룹 인덱스 (01~99 형식, 2자리로 고정)
        // modelGroupIndex는 EquipmentModel 엔티티에 존재한다고 가정
        String modelGroupIndexPart = String.format("%02d", model.getModelGroupIndex());

        // 접두사 구성: 카테고리3 + 모델3 + 모델그룹인덱스2 + 날짜4 = 총 12자리
        String prefix = prefixCategoryCode + prefixEquipmentModelCode + modelGroupIndexPart; // e.g., CAMSON01

        // 날짜 구성: yyMM (현재 시간 기준)
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM")); // e.g., 2506

        return prefix + datePrefix; // e.g., CAMSON012506
    }

    /**
     * 시리얼 넘버의 전체를 생성합니다. (접두사 + 동적 자릿수 시퀀스)
     *
     * @param category 장비 카테고리 엔티티
     * @param model    장비 모델 엔티티
     * @param currentCount 현재까지 해당 접두사로 생성된 가장 큰 시퀀스 번호
     * @return 생성된 최종 시리얼 넘버 (예: CAMSON01250601, CAMSON012506100, CAMSON0125061000)
     */
    private String generateUniqueSerialNumber(EquipmentCategory category, EquipmentModel model, long currentCount) {
        String serialPrefix = generateSerialPrefix(category, model); // 새로운 접두사 생성 메소드 사용

        String sequenceFormat;
        if (currentCount < 9) { // 0 ~ 9 (다음 번호가 1~10일 때)
            sequenceFormat = "%02d"; // 01 ~ 10
        } else if (currentCount < 99) { // 10 ~ 99 (다음 번호가 100~999일 때)
            sequenceFormat = "%03d"; // 100 ~ 999
        } else if (currentCount < 999) { // 100 ~ 999 (다음 번호가 1000~9999일 때)
            sequenceFormat = "%04d"; // 1000 ~ 9999
        } else if (currentCount < 9999) { // 1000 ~ 9999 (다음 번호가 10000~99999일 때)
            sequenceFormat = "%05d"; // 10000 ~ 99999
        } else {
            sequenceFormat = "%06d"; // 필요에 따라 더 확장
        }

        String sequence = String.format(sequenceFormat, currentCount + 1);

        // 최종 시리얼 넘버 (총 길이 가변)
        // CAMSON012506 + 01 = 14자리 (CAMSON01250601)
        // CAMSON012506 + 100 = 15자리 (CAMSON012506100)
        return serialPrefix + sequence;
    }

    /**
     * 장비 등록 전 시리얼 넘버를 미리보거나 단일 시리얼 넘버를 생성하는 메소드.
     * 트랜잭션 없이 읽기 전용으로 동작.
     *
     * @param request 시리얼 넘버 생성 요청 DTO
     * @return 생성될 다음 시리얼 넘버 문자열
     * @throws RuntimeException 카테고리 또는 모델을 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public String generateSerialNumber(AdminEquipmentSerialNumberGenerateRequest request) {
        EquipmentCategory category = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        EquipmentModel model = equipmentModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new RuntimeException("모델 없음"));

        String serialPrefix = generateSerialPrefix(category, model); // 새로운 접두사 생성 메소드 사용

        long currentMaxSequence = 0;

        List<Equipment> existingEquipments = equipmentRepository.findBySerialNumberStartingWith(serialPrefix);
        List<String> existingSerialNumbers = existingEquipments.stream()
                .map(Equipment::getSerialNumber)
                .collect(Collectors.toList());

        if (!existingSerialNumbers.isEmpty()) {
            currentMaxSequence = existingSerialNumbers.stream()
                    .map(sn -> {
                        try {
                            String sequencePart = sn.substring(serialPrefix.length());
                            return Long.parseLong(sequencePart);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.err.println("Error parsing sequence number for preview: " + sn + " with prefix: " + serialPrefix + " - " + e.getMessage());
                            return 0L;
                        }
                    })
                    .max(Long::compare)
                    .orElse(0L);
        }

        return generateUniqueSerialNumber(category, model, currentMaxSequence);
    }


    /**
     * 여러 개의 장비를 생성하고 저장하는 메소드.
     *
     * @param request 장비 생성 요청 DTO
     * @return 저장된 장비들의 ID 리스트
     * @throws RuntimeException 카테고리, 모델을 찾을 수 없거나 시리얼 넘버 중복 발생 시
     */
    @Transactional
    public List<Long> createEquipment(AdminEquipmentCreateRequest request) {
        EquipmentCategory category = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        EquipmentModel model = equipmentModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new RuntimeException("모델 없음"));

        String serialPrefix = generateSerialPrefix(category, model); // 새로운 접두사 생성 메소드 사용

        long currentMaxSequence = 0;

        List<Equipment> existingEquipments = equipmentRepository.findBySerialNumberStartingWith(serialPrefix);
        List<String> existingSerialNumbers = existingEquipments.stream()
                .map(Equipment::getSerialNumber)
                .collect(Collectors.toList());

        if (!existingSerialNumbers.isEmpty()) {
            currentMaxSequence = existingSerialNumbers.stream()
                    .map(sn -> {
                        try {
                            String sequencePart = sn.substring(serialPrefix.length());
                            return Long.parseLong(sequencePart);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.err.println("Error parsing sequence number during creation: " + sn + " with prefix: " + serialPrefix + " - " + e.getMessage());
                            return 0L;
                        }
                    })
                    .max(Long::compare)
                    .orElse(0L);
        }

        List<Long> savedEquipmentIds = new ArrayList<>();

        for (int i = 1; i <= request.getQuantity(); i++) {
            String serialNumber = generateUniqueSerialNumber(category, model, currentMaxSequence + (i - 1));
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
        Pageable pageable = request.toPageable();

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

        for (Long equipmentId : request.getIds()) {
            operator.apply(equipmentId, request.getDetail());
        }

        return request.getIds();
    }

    //장비 상태 파손 처리
    @Transactional
    public Long changeToBrokenStatus(Long equipmentId, String detail, LoginUser loginUser){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(()->new IllegalArgumentException("관리자를 찾을 수 없습니다"));
        if (equipment.getStatus() == Status.BROKEN)
            throw new IllegalArgumentException("이미 파손된 강의실입니다.");

        equipment.makeBroken();
        equipmentRepository.save(equipment);

        BrokenRepairHistory history = BrokenRepairHistory.markAsBrokenEquipmentByAdmin(equipment, user, detail);
        brokenRepairHistoryRepository.save(history);
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

        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 최근 고장 이력 선택 (없을 수도 있음)
        Optional<BrokenRepairHistory> latestBroken =
                brokenRepairHistoryRepository.findTopByEquipmentAndHistoryTypeOrderByCreatedAtDesc(
                        equipment, BrokenRepairHistory.HistoryType.BROKEN
                );

        // 수리 이력 등록
        BrokenRepairHistory repairHistory = BrokenRepairHistory.markAsRepairEquipment(
                equipment, user, detail, latestBroken.orElse(null)
        );
        brokenRepairHistoryRepository.save(repairHistory);

        // 장비 상태 변경
        equipment.makeAvailable();
        equipmentRepository.save(equipment);

        return equipmentId;
    }






}