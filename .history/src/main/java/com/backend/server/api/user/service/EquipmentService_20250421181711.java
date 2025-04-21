package com.backend.server.api.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.user.dto.CancelRequestList;
import com.backend.server.api.user.dto.EquipmentListRequest;
import com.backend.server.api.user.dto.EquipmentListResponse;
import com.backend.server.api.user.dto.EquipmentResponse;
import com.backend.server.api.user.dto.RentalListRequest;
import com.backend.server.api.user.dto.RentalListResponse;
import com.backend.server.api.user.dto.RentalRequest;
import com.backend.server.api.user.dto.RentalResponse;
import com.backend.server.api.user.dto.ReturnListRequest;
import com.backend.server.api.user.dto.ReturnRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.Favorite;
import com.backend.server.model.entity.RentalRequest.RentalRequestStatus;
import com.backend.server.model.entity.RentalRequest;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.FavoriteRepository;
import com.backend.server.model.repository.RentalRequestRepository;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final RentalRequestRepository rentalRequestRepository;
    private final FavoriteRepository favoriteRepository;
    private final SecurityUtil securityUtil;

    // 장비 목록 조회
    public EquipmentListResponse getEquipments(EquipmentListRequest request, Long userId) {
        Pageable pageable = EquipmentSpecification.getPageable(request);
        // 유저학년찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Long grade = Long.parseLong(user.getGrade());
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, grade);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return new EquipmentListResponse(page);
    }
    
    // 장비 상세 조회
    public EquipmentResponse getEquipment(Long equipmentId, Long userId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        // 사용자가 즐겨찾기에 추가했는지 확인
        boolean isFavorite = favoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId);
        
        return EquipmentResponse.fromEntity(equipment, isFavorite);
    }
    
    // 장비 대여 요청
    @Transactional
    public RentalResponse createRentRequest(Long userId, RentalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        // 대여 가능한 상태인지 확인
        if (!equipment.isAvailable()) {
            throw new RuntimeException("현재 대여 불가능한 장비입니다.");
        }
        
        RentalRequest rentalRequest = RentalRequest.builder()
                .user(user)
                .equipment(equipment)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .purpose(request.getPurpose())
                .status(RentalRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        
        RentalRequest savedRequest = rentalRequestRepository.save(rentalRequest);
        return RentalResponse.fromEntity(savedRequest);
    }
    
    // 다중 장비 대여 요청
    @Transactional
    public RentalListResponse createBulkRentRequests(Long userId, RentalListRequest requestList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        List<RentalResponse> responses = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();
        
        for (RentalRequest request : requestList.getRequests()) {
            try {
                Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                
                if (!equipment.isAvailable()) {
                    failureMessages.add("장비 ID " + request.getEquipmentId() + ": 현재 대여 불가능한 장비입니다.");
                    continue;
                }
                
                RentalRequest rentalRequest = RentalRequest.builder()
                        .user(user)
                        .equipment(equipment)
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .purpose(request.getPurpose())
                        .status(RentalRequestStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();
                
                RentalRequest savedRequest = rentalRequestRepository.save(rentalRequest);
                responses.add(RentalResponse.fromEntity(savedRequest));
            } catch (Exception e) {
                failureMessages.add("장비 ID " + request.getEquipmentId() + ": " + e.getMessage());
            }
        }
        
        return new RentalListResponse(responses, failureMessages);
    }
    
    // 장비 반납 요청
    @Transactional
    public RentalResponse createReturnRequest(Long userId, ReturnRequest request) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(request.getRentalRequestId())
                .orElseThrow(() -> new RuntimeException("대여 요청을 찾을 수 없습니다."));
        
        // 본인의 대여 요청인지 확인
        if (!rentalRequest.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 대여 요청만 반납 요청할 수 있습니다.");
        }
        
        // 대여 중인 상태인지 확인
        if (rentalRequest.getStatus() != RentalRequestStatus.APPROVED) {
            throw new RuntimeException("대여 중인 장비만 반납 요청할 수 있습니다.");
        }
        
        // 상태 변경
        rentalRequest.setStatus(RentalRequestStatus.RETURN_REQUESTED);
        RentalRequest updatedRequest = rentalRequestRepository.save(rentalRequest);
        
        return RentalResponse.fromEntity(updatedRequest);
    }
    
    // 다중 장비 반납 요청
    @Transactional
    public RentalListResponse createBulkReturnRequests(Long userId, ReturnListRequest requestList) {
        List<RentalResponse> responses = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();
        
        for (ReturnRequest request : requestList.getRequests()) {
            try {
                RentalRequest rentalRequest = rentalRequestRepository.findById(request.getRentalRequestId())
                        .orElseThrow(() -> new RuntimeException("대여 요청을 찾을 수 없습니다."));
                
                // 본인의 대여 요청인지 확인
                if (!rentalRequest.getUser().getId().equals(userId)) {
                    failureMessages.add("대여 요청 ID " + request.getRentalRequestId() + ": 본인의 대여 요청만 반납 요청할 수 있습니다.");
                    continue;
                }
                
                // 대여 중인 상태인지 확인
                if (rentalRequest.getStatus() != RentalRequestStatus.APPROVED) {
                    failureMessages.add("대여 요청 ID " + request.getRentalRequestId() + ": 대여 중인 장비만 반납 요청할 수 있습니다.");
                    continue;
                }
                
                // 상태 변경
                rentalRequest.setStatus(RentalRequestStatus.RETURN_REQUESTED);
                RentalRequest updatedRequest = rentalRequestRepository.save(rentalRequest);
                
                responses.add(RentalResponse.fromEntity(updatedRequest));
            } catch (Exception e) {
                failureMessages.add("대여 요청 ID " + request.getRentalRequestId() + ": " + e.getMessage());
            }
        }
        
        return new RentalListResponse(responses, failureMessages);
    }
    
    // 대여/반납 요청 취소
    @Transactional
    public void cancelRentalRequest(Long userId, Long requestId) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));
        
        // 본인의 요청인지 확인
        if (!rentalRequest.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 요청만 취소할 수 있습니다.");
        }
        
        // 취소 가능한 상태인지 확인 (대기 중이거나 반납 요청 중인 상태)
        if (rentalRequest.getStatus() != RentalRequestStatus.PENDING && 
            rentalRequest.getStatus() != RentalRequestStatus.RETURN_REQUESTED) {
            throw new RuntimeException("취소할 수 없는 상태입니다.");
        }
        
        if (rentalRequest.getStatus() == RentalRequestStatus.PENDING) {
            // 대여 요청 취소의 경우 삭제
            rentalRequestRepository.delete(rentalRequest);
        } else {
            // 반납 요청 취소의 경우 상태를 다시 대여 중으로 변경
            rentalRequest.setStatus(RentalRequestStatus.APPROVED);
            rentalRequestRepository.save(rentalRequest);
        }
    }
    
    // 다중 대여/반납 요청 취소
    @Transactional
    public List<Long> cancelBulkRentalRequests(Long userId, CancelRequestList cancelList) {
        List<Long> canceledIds = new ArrayList<>();
        
        for (Long requestId : cancelList.getRequestIds()) {
            try {
                RentalRequest rentalRequest = rentalRequestRepository.findById(requestId)
                        .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));
                
                // 본인의 요청인지 확인
                if (!rentalRequest.getUser().getId().equals(userId)) {
                    continue;
                }
                
                // 취소 가능한 상태인지 확인 (대기 중이거나 반납 요청 중인 상태)
                if (rentalRequest.getStatus() != RentalRequestStatus.PENDING && 
                    rentalRequest.getStatus() != RentalRequestStatus.RETURN_REQUESTED) {
                    continue;
                }
                
                if (rentalRequest.getStatus() == RentalRequestStatus.PENDING) {
                    // 대여 요청 취소의 경우 삭제
                    rentalRequestRepository.delete(rentalRequest);
                } else {
                    // 반납 요청 취소의 경우 상태를 다시 대여 중으로 변경
                    rentalRequest.setStatus(RentalRequestStatus.APPROVED);
                    rentalRequestRepository.save(rentalRequest);
                }
                
                canceledIds.add(requestId);
            } catch (Exception e) {
                // 실패한 경우 무시하고 다음 요청 처리
            }
        }
        
        return canceledIds;
    }
    
    // 즐겨찾기 추가
    @Transactional
    public void addFavorite(Long userId, Long equipmentId) {
        // 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 장비 확인
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        // 이미 즐겨찾기에 있는지 확인
        if (favoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId)) {
            throw new RuntimeException("이미 즐겨찾기에 추가된 장비입니다.");
        }
        
        // 즐겨찾기 추가
        Favorite favorite = Favorite.builder()
                .user(user)
                .equipment(equipment)
                .createdAt(LocalDateTime.now())
                .build();
        
        favoriteRepository.save(favorite);
    }
    
    // 즐겨찾기 삭제
    @Transactional
    public void removeFavorite(Long userId, Long equipmentId) {
        // 사용자의 해당 장비 즐겨찾기 삭제
        Favorite favorite = favoriteRepository.findByUserIdAndEquipmentId(userId, equipmentId)
                .orElseThrow(() -> new RuntimeException("즐겨찾기에 추가되지 않은 장비입니다."));
        
        favoriteRepository.delete(favorite);
    }
}
