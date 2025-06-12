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

    // 시리얼 넘버를 생성하는 공통 로직 (모델 그룹 인덱스 및 동적 자릿수 포함)
    private String generateUniqueSerialNumber(EquipmentCategory category, EquipmentModel model, long currentCount) {
        // 카테고리 영문코드 3자리 (3자리 미만이면 _로 채움)
        String prefixCategoryCode;
        String categoryCode = category.getEnglishCode().toUpperCase();
        if (categoryCode.length() < 3) {
            prefixCategoryCode = String.format("%3s", categoryCode).replace(' ', '_');
        } else {
            prefixCategoryCode = categoryCode.substring(0, 3);
        }

        // 모델 영문 코드 3자리 (3자리 미만이면 _로 채움)
        String prefixModelEnglishCode;
        String modelEnglishCode = model.getEnglishCode().toUpperCase();
        if (modelEnglishCode.length() < 3) {
            prefixModelEnglishCode = String.format("%3s", modelEnglishCode).replace(' ', '_');
        } else {
            prefixModelEnglishCode = modelEnglishCode.substring(0, 3);
        }

        // 모델 그룹 인덱스 (EquipmentModel 엔티티에 modelGroupIndex 필드가 있다고 가정)
        // modelGroupIndex가 null일 경우를 대비하여 0으로 처리 (일반적으로는 모델 생성 시 부여되므로 null이 아니어야 함)
        String modelGroupIndexStr = String.format("%02d", Optional.ofNullable(model.getModelGroupIndex()).orElse(0)); // 2자리로 고정

        // 날짜 구성: yyMM (현재 2025년 6월 12일 -> 2506)
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

        // 시퀀스 번호 자릿수 동적 결정
        String sequenceFormat;
        if (currentCount < 9) { // 0 ~ 9
            sequenceFormat = "%01d"; // 1자리
        } else if (currentCount < 99) { // 10 ~ 99
            sequenceFormat = "%02d"; // 2자리
        } else if (currentCount < 999) { // 100 ~ 999
            sequenceFormat = "%03d"; // 3자리
        } else if (currentCount < 9999) { // 1000 ~ 9999
            sequenceFormat = "%04d"; // 4자리
        } else {
            // 9999를 초과하는 경우 5자리 이상으로 확장. 필요에 따라 더 많은 자릿수를 정의하거나,
            // 시리얼 넘버 길이 제한이 있다면 예외 처리 등을 고려할 수 있습니다.
            sequenceFormat = "%05d"; // 예를 들어 5자리까지 확장
        }

        String sequence = String.format(sequenceFormat, currentCount + 1); // currentCount는 0부터 시작하므로 +1

        // 최종 시리얼 넘버 조합
        // 예: CAMCAN01250601 (카테고리3 + 모델영문3 + 모델그룹2 + 날짜4 + 시퀀스2 = 총 14자리)
        // 예: _CA_A_01250601 (카테고리3 + 모델영문3 + 모델그룹2 + 날짜4 + 시퀀스2 = 총 14자리)
        return prefixCategoryCode + prefixModelEnglishCode + modelGroupIndexStr + datePrefix + sequence;
    }


    // 단일 시리얼 넘버를 미리 생성하여 반환
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public String generateSerialNumber(AdminEquipmentSerialNumberGenerateRequest request) {
        EquipmentCategory category = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        EquipmentModel model = equipmentModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new RuntimeException("모델 없음"));

        // generateUniqueSerialNumber가 사용하는 접두사를 계산
        String prefixCategoryCode;
        String categoryCode = category.getEnglishCode().toUpperCase();
        if (categoryCode.length() < 3) {
            prefixCategoryCode = String.format("%3s", categoryCode).replace(' ', '_');
        } else {
            prefixCategoryCode = categoryCode.substring(0, 3);
        }

        String prefixModelEnglishCode;
        String modelEnglishCode = model.getEnglishCode().toUpperCase();
        if (modelEnglishCode.length() < 3) {
            prefixModelEnglishCode = String.format("%3s", modelEnglishCode).replace(' ', '_');
        } else {
            prefixModelEnglishCode = modelEnglishCode.substring(0, 3);
        }

        String modelGroupIndexStr = String.format("%02d", Optional.ofNullable(model.getModelGroupIndex()).orElse(0));
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

        // 쿼리용 시리얼 접두사 (시퀀스 부분을 제외한 고정 부분)
        String serialPrefixForQuery = prefixCategoryCode + prefixModelEnglishCode + modelGroupIndexStr + datePrefix;

        // 현재 이 접두사로 시작하는 가장 큰 시리얼 넘버를 가져와서 시퀀스를 파악
        Optional<String> latestSerialNumberOpt = equipmentRepository.findTopBySerialNumberStartingWithOrderBySerialNumberDesc(serialPrefixForQuery);
        long currentMaxSequence = 0;
        if (latestSerialNumberOpt.isPresent()) {
            String latestSerialNumber = latestSerialNumberOpt.get();
            try {
                // 접두사를 제외한 나머지 부분이 시퀀스 번호
                currentMaxSequence = Long.parseLong(latestSerialNumber.substring(serialPrefixForQuery.length()));
            } catch (NumberFormatException e) {
                // 시리얼 넘버 뒷부분이 숫자가 아닌 경우 (예외 상황)
                System.err.println("Error parsing sequence number from " + latestSerialNumber + ": " + e.getMessage());
                currentMaxSequence = 0; // 파싱 실패 시 0부터 다시 시작 (또는 다른 에러 처리)
            }
        }

        // 다음 시리얼 넘버를 생성
        return generateUniqueSerialNumber(category, model, currentMaxSequence);
    }

    // 장비를 실제로 생성하는 메소드 (createEquipment)
    @Transactional // 트랜잭션 적용
    public List<Long> createEquipment(AdminEquipmentCreateRequest request) {
        EquipmentCategory category = equipmentCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        EquipmentModel model = equipmentModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new RuntimeException("모델 없음"));

        // generateUniqueSerialNumber가 사용하는 접두사를 계산
        String prefixCategoryCode;
        String categoryCode = category.getEnglishCode().toUpperCase();
        if (categoryCode.length() < 3) {
            prefixCategoryCode = String.format("%3s", categoryCode).replace(' ', '_');
        } else {
            prefixCategoryCode = categoryCode.substring(0, 3);
        }

        String prefixModelEnglishCode;
        String modelEnglishCode = model.getEnglishCode().toUpperCase();
        if (modelEnglishCode.length() < 3) {
            prefixModelEnglishCode = String.format("%3s", modelEnglishCode).replace(' ', '_');
        } else {
            prefixModelEnglishCode = modelEnglishCode.substring(0, 3);
        }

        String modelGroupIndexStr = String.format("%02d", Optional.ofNullable(model.getModelGroupIndex()).orElse(0));
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));

        // 쿼리용 시리얼 접두사 (시퀀스 부분을 제외한 고정 부분)
        String serialPrefixForQuery = prefixCategoryCode + prefixModelEnglishCode + modelGroupIndexStr + datePrefix;

        // 트랜잭션 내에서 가장 최신 시퀀스 번호를 가져와서 동시성 문제 완화
        // findTopBySerialNumberStartingWithOrderBySerialNumberDesc 쿼리가 필요
        Optional<String> latestSerialNumberOpt = equipmentRepository.findTopBySerialNumberStartingWithOrderBySerialNumberDesc(serialPrefixForQuery);
        long currentMaxSequence = 0;
        if (latestSerialNumberOpt.isPresent()) {
            String latestSerialNumber = latestSerialNumberOpt.get();
            try {
                // 접두사를 제외한 나머지 부분이 시퀀스 번호
                currentMaxSequence = Long.parseLong(latestSerialNumber.substring(serialPrefixForQuery.length()));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing sequence number from " + latestSerialNumber + ": " + e.getMessage());
                currentMaxSequence = 0;
            }
        }

        List<Long> savedEquipmentIds = new ArrayList<>();

        for (int i = 1; i <= request.getQuantity(); i++) {
            // 각 장비마다 증가된 시퀀스 번호를 사용하여 시리얼 넘버 생성
            // generateUniqueSerialNumber에 전달하는 currentCount는 0부터 시작하므로 (i-1)을 사용
            String serialNumber = generateUniqueSerialNumber(category, model, currentMaxSequence + (i - 1));

            // DB unique 제약조건에 의해 에러 발생하기 전에 미리 확인 (선택 사항이지만 안전한 처리)
            // 동시성 문제 발생 시 여기서 충돌 감지 및 예외 처리
            if (equipmentRepository.findBySerialNumber(serialNumber).isPresent()) {
                throw new RuntimeException("Generated serial number " + serialNumber + " already exists. Possible concurrency issue. Please retry.");
            }

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
