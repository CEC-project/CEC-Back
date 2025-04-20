package com.backend.server.api.user.service;

import com.backend.server.api.user.dto.*;
import com.backend.server.exception.BadRequestException;
import com.backend.server.exception.ForbiddenException;
import com.backend.server.exception.NotFoundException;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.Favorite;
import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalHistoryStatus;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.FavoriteRepository;
import com.backend.server.model.repository.RentalHistoryRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RentalHistoryRepository rentalHistoryRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public EquipmentListResponse getEquipments(EquipmentListRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        
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
        
        List<Equipment> equipments;
        // 즐겨찾기 필터링 처리
        if (request.getFavoriteOnly() != null && request.getFavoriteOnly()) {
            // 즐겨찾기한 장비만 조회
            List<Equipment> favoriteEquipments = equipmentRepository.findAllFavoritesByUserId(userId);
            // 즐겨찾기 장비에 대해 추가 필터링 조건 적용
            equipments = favoriteEquipments.stream()
                    .filter(e -> spec.toPredicate(null, null, null) == null)
                    .collect(Collectors.toList());
        } else {
            // 일반 조회
            equipments = equipmentRepository.findAll(spec, sort);
        }
        
        // 즐겨찾기 정보 조회
        List<Favorite> favorites = favoriteRepository.findAllByUserId(userId);
        Map<Long, Favorite> favoriteMap = favorites.stream()
                .collect(Collectors.toMap(Favorite::getEquipmentId, f -> f));
        
        // 응답 DTO 변환
        List<EquipmentDto> equipmentDtos = equipments.stream()
                .map(equipment -> {
                    boolean isFavorite = favoriteMap.containsKey(equipment.getId());
                    return EquipmentDto.fromEntity(equipment, isFavorite);
                })
                .collect(Collectors.toList());
        
        return new EquipmentListResponse(equipmentDtos);
    }

    @Transactional(readOnly = true)
    public EquipmentResponse getEquipment(Long equipmentId, Long userId) {
        Equipment equipment = equipmentRepository.findByIdWithRentalHistories(equipmentId)
                .orElseThrow(() -> new NotFoundException("장비를 찾을 수 없습니다."));
        
        boolean isFavorite = favoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId);
        
        return EquipmentResponse.fromEntity(equipment, isFavorite);
    }

    @Transactional
    public RentalResponse createRentRequest(Long userId, RentalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
                
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException("장비를 찾을 수 없습니다."));
                
        // 대여 가능 여부 확인
        if (!equipment.isAvailable()) {
            throw new BadRequestException("현재 대여할 수 없는 장비입니다.");
        }
        
        // 대여 제한 학년 확인
        if (equipment.getRentalRestrictedGrades() != null && !equipment.getRentalRestrictedGrades().isEmpty()) {
            if (equipment.getRentalRestrictedGrades().contains(String.valueOf(user.getGrade()))) {
                throw new ForbiddenException("현재 학년은 이 장비를 대여할 수 없습니다.");
            }
        }
        
        // 대여 기록 생성
        RentalHistory rentalHistory = RentalHistory.builder()
                .user(user)
                .equipment(equipment)
                .status(RentalHistoryStatus.PENDING)
                .rentRequestDate(LocalDateTime.now())
                .expectedReturnDate(request.getExpectedReturnDate())
                .purpose(request.getPurpose())
                .build();
                
        rentalHistoryRepository.save(rentalHistory);
        
        return RentalResponse.fromEntity(rentalHistory);
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
        RentalHistory rentalHistory = rentalHistoryRepository.findById(request.getRentalId())
                .orElseThrow(() -> new NotFoundException("대여 정보를 찾을 수 없습니다."));
                
        // 권한 확인
        if (!rentalHistory.getUser().getId().equals(userId)) {
            throw new ForbiddenException("자신의 대여 기록만 반납 요청할 수 있습니다.");
        }
        
        // 상태 확인
        if (rentalHistory.getStatus() != RentalHistoryStatus.RENTED) {
            throw new BadRequestException("대여 중인 장비만 반납 요청할 수 있습니다.");
        }
        
        // 상태 변경
        rentalHistory.setStatus(RentalHistoryStatus.RETURN_PENDING);
        rentalHistory.setReturnRequestDate(LocalDateTime.now());
        rentalHistory.setReturnNote(request.getReturnNote());
        
        rentalHistoryRepository.save(rentalHistory);
        
        return RentalResponse.fromEntity(rentalHistory);
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
        RentalHistory rentalHistory = rentalHistoryRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("요청 정보를 찾을 수 없습니다."));
                
        // 권한 확인
        if (!rentalHistory.getUser().getId().equals(userId)) {
            throw new ForbiddenException("자신의 요청만 취소할 수 있습니다.");
        }
        
        // 상태 확인
        if (rentalHistory.getStatus() != RentalHistoryStatus.PENDING && 
            rentalHistory.getStatus() != RentalHistoryStatus.RETURN_PENDING) {
            throw new BadRequestException("대여 요청 중이거나 반납 요청 중인 상태만 취소할 수 있습니다.");
        }
        
        // 대여 요청 취소인 경우
        if (rentalHistory.getStatus() == RentalHistoryStatus.PENDING) {
            rentalHistoryRepository.delete(rentalHistory);
        } 
        // 반납 요청 취소인 경우
        else if (rentalHistory.getStatus() == RentalHistoryStatus.RETURN_PENDING) {
            rentalHistory.setStatus(RentalHistoryStatus.RENTED);
            rentalHistory.setReturnRequestDate(null);
            rentalHistory.setReturnNote(null);
            rentalHistoryRepository.save(rentalHistory);
        }
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
        // 장비 존재 확인
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new NotFoundException("장비를 찾을 수 없습니다.");
        }
        
        // 이미 즐겨찾기 되어 있는지 확인
        if (favoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId)) {
            return; // 이미 즐겨찾기된 경우 아무것도 하지 않음
        }
        
        // 즐겨찾기 추가
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .equipmentId(equipmentId)
                .createdAt(LocalDateTime.now())
                .build();
                
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long userId, Long equipmentId) {
        favoriteRepository.deleteByUserIdAndEquipmentId(userId, equipmentId);
    }
}
