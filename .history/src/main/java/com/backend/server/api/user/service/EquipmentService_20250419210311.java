package com.backend.server.api.user.service;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.api.user.dto.*;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.Favorite;
import com.backend.server.model.entity.RentalEntity;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.FavoriteRepository;
import com.backend.server.model.repository.RentalRepository;
import com.backend.server.model.specification.EquipmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final FavoriteRepository favoriteRepository;
    private final RentalRepository rentalRepository;

    /**
     * 장비 목록 조회 (사용자별 권한에 따라 다른 필터링 적용)
     */
    public EquipmentListResponse getEquipments(EquipmentListRequest request, Long userId, Integer userGrade, boolean isAdmin) {
        // 페이징 및 정렬 설정
        Pageable pageable = createPageable(request);
        
        // 사용자 권한에 따른 필터링
        Specification<Equipment> spec;
        if (isAdmin) {
            // 관리자는 모든 장비 조회 가능
            spec = EquipmentSpecification.filterEquipments(request);
        } else {
            // 일반 사용자는 학년 제한 확인
            spec = EquipmentSpecification.filterEquipments2(request, userGrade);
        }
        
        // 장비 목록 조회
        Page<Equipment> equipmentPage = equipmentRepository.findAll(spec, pageable);
        
        // 즐겨찾기 정보 조회
        List<Long> equipmentIds = equipmentPage.getContent().stream()
                .map(Equipment::getId)
                .collect(Collectors.toList());
        
        List<Favorite> favorites = favoriteRepository.findByUserIdAndEquipmentIdIn(userId, equipmentIds);
        Map<Long, Boolean> favoriteMap = favorites.stream()
                .collect(Collectors.toMap(Favorite::getEquipmentId, favorite -> true));
        
        // DTO 변환
        List<EquipmentResponse> content = equipmentPage.getContent().stream()
                .map(equipment -> {
                    EquipmentResponse response = new EquipmentResponse(equipment);
                    response.setIsFavorite(favoriteMap.getOrDefault(equipment.getId(), false));
                    return response;
                })
                .collect(Collectors.toList());
        
        // 응답 생성
        PageableInfo pageInfo = new PageableInfo(
                equipmentPage.getNumber(), 
                equipmentPage.getSize(), 
                equipmentPage.getTotalPages(), 
                equipmentPage.getTotalElements()
        );
        
        return new EquipmentListResponse(content, pageInfo);
    }
    
    /**
     * 장비 상세 조회
     */
    public EquipmentResponse getEquipment(Long equipmentId, Long userId) {
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장비입니다."));
        
        // DTO 변환
        EquipmentResponse response = new EquipmentResponse(equipment);
        
        // 즐겨찾기 여부 확인
        boolean isFavorite = favoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId);
        response.setIsFavorite(isFavorite);
        
        return response;
    }
    
    /**
     * 대여 신청 (단일 장비)
     */
    @Transactional
    public RentalResponse createRentRequest(Long userId, RentRequest request) {
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장비입니다."));
        
        // 대여 가능 여부 확인
        if (!equipment.getAvailable()) {
            throw new IllegalStateException("현재 대여가 불가능한 장비입니다.");
        }
        
        // 대여 신청 생성
        RentalEntity rental = RentalEntity.builder()
                .equipmentId(equipment.getId())
                .userId(userId)
                .rentalTime(request.getRentalTime())
                .returnTime(request.getReturnTime())
                .purpose(request.getPurpose())
                .status(0) // PENDING
                .build();
        
        RentalEntity savedRental = rentalRepository.save(rental);
        
        // 응답 생성
        return RentalResponse.builder()
                .id(savedRental.getId())
                .equipmentId(savedRental.getEquipmentId())
                .equipmentName(equipment.getName())
                .rentalTime(savedRental.getRentalTime())
                .returnTime(savedRental.getReturnTime())
                .status(savedRental.getStatus())
                .createdAt(savedRental.getCreatedAt())
                .build();
    }
    
    /**
     * 대여 신청 (다중 장비)
     */
    @Transactional
    public RentalResponseList createBulkRentRequests(Long userId, RentRequestList requestList) {
        List<RentalResponse> responses = requestList.getRequests().stream()
                .map(request -> createRentRequest(userId, request))
                .collect(Collectors.toList());
        
        return new RentalResponseList(responses);
    }
    
    /**
     * 대여 신청 (일괄 대여)
     */
    @Transactional
    public RentalResponseList createMultipleRental(Long userId, RentalListRequest request) {
        // 각 장비 대여 신청
        List<RentalResponse> responses = request.getEquipmentIds().stream()
                .map(equipmentId -> {
                    // 장비 조회
                    Equipment equipment = equipmentRepository.findById(equipmentId)
                            .orElseThrow(() -> new IllegalArgumentException("장비 ID " + equipmentId + "는 존재하지 않습니다."));
                    
                    // 대여 가능 여부 확인
                    if (!equipment.getAvailable()) {
                        throw new IllegalStateException("장비 ID " + equipmentId + "는 현재 대여가 불가능합니다.");
                    }
                    
                    // 대여 신청 생성
                    RentalEntity rental = RentalEntity.builder()
                            .equipmentId(equipmentId)
                            .userId(userId)
                            .rentalTime(request.getStartTime())
                            .returnTime(request.getEndTime())
                            .status(0) // PENDING
                            .build();
                    
                    RentalEntity savedRental = rentalRepository.save(rental);
                    
                    // 응답 생성
                    return RentalResponse.builder()
                            .id(savedRental.getId())
                            .equipmentId(savedRental.getEquipmentId())
                            .equipmentName(equipment.getName())
                            .rentalTime(savedRental.getRentalTime())
                            .returnTime(savedRental.getReturnTime())
                            .status(savedRental.getStatus())
                            .createdAt(savedRental.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
        
        return new RentalResponseList(responses);
    }
    
    /**
     * 반납 요청 (단일 장비)
     */
    @Transactional
    public RentalResponse createReturnRequest(Long userId, ReturnRequest request) {
        // 대여 조회
        RentalEntity rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 정보입니다."));
        
        // 대여 사용자 확인
        if (!rental.getUserId().equals(userId)) {
            throw new IllegalArgumentException("대여 신청자만 반납 요청을 할 수 있습니다.");
        }
        
        // 대여 상태 확인
        if (rental.getStatus() != 1) { // APPROVED가 아닌 경우
            throw new IllegalStateException("승인된 대여만 반납 요청할 수 있습니다.");
        }
        
        // 반납 상태 및 정보 업데이트
        rental.setReturnCondition(request.getReturnCondition());
        rental.setStatus(2); // RETURN_REQUESTED
        
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(rental.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장비입니다."));
        
        // 응답 생성
        return RentalResponse.builder()
                .id(rental.getId())
                .equipmentId(rental.getEquipmentId())
                .equipmentName(equipment.getName())
                .rentalTime(rental.getRentalTime())
                .returnTime(rental.getReturnTime())
                .status(rental.getStatus())
                .createdAt(rental.getCreatedAt())
                .build();
    }
    
    /**
     * 반납 요청 (다중 장비)
     */
    @Transactional
    public RentalResponseList createBulkReturnRequests(Long userId, ReturnRequestList requestList) {
        List<RentalResponse> responses = requestList.getRequests().stream()
                .map(request -> createReturnRequest(userId, request))
                .collect(Collectors.toList());
        
        return new RentalResponseList(responses);
    }
    
    /**
     * 즐겨찾기 추가
     */
    @Transactional
    public void addFavorite(Long userId, Long equipmentId) {
        // 장비 존재 확인
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new IllegalArgumentException("존재하지 않는 장비입니다.");
        }
        
        // 이미 즐겨찾기에 있는지 확인
        if (favoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId)) {
            throw new IllegalStateException("이미 즐겨찾기에 추가된 장비입니다.");
        }
        
        // 즐겨찾기 추가
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .equipmentId(equipmentId)
                .build();
        
        favoriteRepository.save(favorite);
    }
    
    /**
     * 즐겨찾기 제거
     */
    @Transactional
    public void removeFavorite(Long userId, Long equipmentId) {
        favoriteRepository.deleteByUserIdAndEquipmentId(userId, equipmentId);
    }
    
    /**
     * 요청 취소
     */
    @Transactional
    public void cancelRentalRequest(Long userId, Long requestId) {
        // 대여 조회
        RentalEntity rental = rentalRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 정보입니다."));
        
        // 대여 사용자 확인
        if (!rental.getUserId().equals(userId)) {
            throw new IllegalArgumentException("대여 신청자만 취소할 수 있습니다.");
        }
        
        // 대여 상태 확인
        if (rental.getStatus() != 0) { // PENDING이 아닌 경우
            throw new IllegalStateException("대기 중인 요청만 취소할 수 있습니다.");
        }
        
        // 대여 요청 삭제
        rentalRepository.deleteById(requestId);
    }
    
    /**
     * 다중 요청 취소
     */
    @Transactional
    public List<Long> cancelBulkRentalRequests(Long userId, CancelRequestList cancelList) {
        return cancelList.getRequestIds().stream()
                .filter(requestId -> {
                    try {
                        cancelRentalRequest(userId, requestId);
                        return true;
                    } catch (Exception e) {
                        // 취소 실패한 요청은 무시
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 페이징 및 정렬 정보 생성
     */
    private Pageable createPageable(EquipmentListRequest request) {
        int page = (request.getPage() != null) ? request.getPage() : 0;
        int size = (request.getSize() != null) ? request.getSize() : 10;
        
        // 정렬 설정
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // 기본 정렬
        
        if (request.getSortBy() != null) {
            Sort.Direction direction = 
                    (request.getSortDirection() != null && request.getSortDirection().equalsIgnoreCase("asc")) 
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            
            switch (request.getSortBy().toLowerCase()) {
                case "name":
                    sort = Sort.by(direction, "name");
                    break;
                case "category":
                    sort = Sort.by(direction, "category");
                    break;
                case "status":
                    sort = Sort.by(direction, "status");
                    break;
                default:
                    sort = Sort.by(Sort.Direction.DESC, "createdAt");
            }
        }
        
        return PageRequest.of(page, size, sort);
    }
}
