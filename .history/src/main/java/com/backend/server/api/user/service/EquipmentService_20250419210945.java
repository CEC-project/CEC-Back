package com.backend.server.api.user.service;

import com.backend.server.api.user.dto.*;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.Favorite;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public EquipmentListResponse getEquipments(EquipmentListRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        
        // 사용자 등급(학년)에 따라 필터링 적용
        Integer userGrade = user.getGrade();
        
        // 정렬 조건 설정
        Sort sort = Sort.unsorted();
        if (request.getSortBy() != null) {
            switch (request.getSortBy()) {
                case 0: // 이름순
                    sort = Sort.by(Sort.Direction.ASC, "name");
                    break;
                case 1: // 카테고리순
                    sort = Sort.by(Sort.Direction.ASC, "category");
                    break;
                case 2: // 상태순
                    sort = Sort.by(Sort.Direction.ASC, "status");
                    break;
                default:
                    sort = Sort.by(Sort.Direction.ASC, "id");
            }
        }
        
        // 사용자 필터링 조건 적용
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments2(request, userGrade);
        
        // 장비 목록 조회
        List<Equipment> equipments = equipmentRepository.findAll(spec, sort);
        
        // 응답 DTO 변환
        List<EquipmentDto> equipmentDtos = equipments.stream()
                .map(equipment -> EquipmentDto.fromEntity(equipment, false))
                .collect(Collectors.toList());
        
        return new EquipmentListResponse(equipmentDtos);
    }

    @Transactional(readOnly = true)
    public EquipmentResponse getEquipment(Long equipmentId, Long userId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장비를 찾을 수 없습니다."));
        
        return EquipmentResponse.fromEntity(equipment, false);
    }

    @Transactional
    public RentalResponse createRentRequest(Long userId, RentalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
                
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장비를 찾을 수 없습니다."));
                
        // 대여 가능 여부 확인
        if (!equipment.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 대여할 수 없는 장비입니다.");
        }
        
        // 대여 제한 학년 확인
        if (equipment.getRentalRestrictedGrades() != null && !equipment.getRentalRestrictedGrades().isEmpty()) {
            if (equipment.getRentalRestrictedGrades().contains(String.valueOf(user.getGrade()))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "현재 학년은 이 장비를 대여할 수 없습니다.");
            }
        }
        
        // 대여 기록 생성
        EquipmentRental rental = EquipmentRental.builder()
                .equipmentId(equipment.getId())
                .userId(userId)
                .rentalTime(LocalDateTime.now())
                .returnTime(request.getExpectedReturnDate())
                .status(RentalStatus.PENDING)
                .build();
                
        // TODO: 대여 기록 저장 기능 추가
        // rentalRepository.save(rental);
        
        return RentalResponse.fromEntity(rental);
    }

    @Transactional
    public RentalListResponse createBulkRentRequests(Long userId, RentalListRequest requestList) {
        List<RentalResponse> responses = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        for (RentRequest request : requestList.getRequests()) {
            try {
                RentalRequest rentalRequest = new RentalRequest();
                rentalRequest.setEquipmentId(request.getEquipmentId());
                rentalRequest.setExpectedReturnDate(request.getExpectedReturnDate());
                rentalRequest.setPurpose(request.getPurpose());
                
                RentalResponse response = createRentRequest(userId, rentalRequest);
                responses.add(response);
            } catch (Exception e) {
                errorMessages.add("장비 ID " + request.getEquipmentId() + ": " + e.getMessage());
            }
        }
        
        return new RentalListResponse(responses, errorMessages);
    }

    @Transactional
    public RentalResponse createReturnRequest(Long userId, ReturnRequest request) {
        // TODO: 반납 요청 기능 구현
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "아직 구현되지 않은 기능입니다.");
    }

    @Transactional
    public RentalListResponse createBulkReturnRequests(Long userId, ReturnListRequest requestList) {
        List<RentalResponse> responses = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        for (ReturnRequest request : requestList.getRequests()) {
            try {
                RentalResponse response = createReturnRequest(userId, request);
                responses.add(response);
            } catch (Exception e) {
                errorMessages.add("대여 ID " + request.getRentalId() + ": " + e.getMessage());
            }
        }
        
        return new RentalListResponse(responses, errorMessages);
    }

    @Transactional
    public void cancelRentalRequest(Long userId, Long requestId) {
        // TODO: 대여 취소 기능 구현
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "아직 구현되지 않은 기능입니다.");
    }

    @Transactional
    public List<Long> cancelBulkRentalRequests(Long userId, CancelRequestList cancelList) {
        List<Long> successIds = new ArrayList<>();
        
        for (Long requestId : cancelList.getRequestIds()) {
            try {
                cancelRentalRequest(userId, requestId);
                successIds.add(requestId);
            } catch (Exception e) {
                // 실패한 요청은 무시하고 계속 진행
            }
        }
        
        return successIds;
    }

    @Transactional
    public void addFavorite(Long userId, Long equipmentId) {
        // TODO: 즐겨찾기 추가 기능 구현
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "아직 구현되지 않은 기능입니다.");
    }

    @Transactional
    public void removeFavorite(Long userId, Long equipmentId) {
        // TODO: 즐겨찾기 삭제 기능 구현
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "아직 구현되지 않은 기능입니다.");
    }
}
