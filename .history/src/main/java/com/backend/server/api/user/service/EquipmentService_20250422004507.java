package com.backend.server.api.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.backend.server.api.user.dto.RentalListResponse.FailedRentalInfo;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentFavorite;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.EquipmentFavoriteRepository;
import com.backend.server.model.repository.EquipmentRentalRepository;
import com.backend.server.model.repository.EquipmentRepository;
import com.backend.server.model.repository.EquipmentSpecification;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentRentalRepository equipmentRentalRepository;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final EquipmentFavoriteRepository equipmentFavoriteRepository;
 

    //장비 목록 조회
    public EquipmentListResponse getEquipments(EquipmentListRequest request) {

        Pageable pageable = EquipmentSpecification.getPageable(request);
        //유저학년찾기
        User user = userRepository.findById(securityUtil.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Long grade = Long.parseLong(user.getGrade());
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, grade);
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        return new EquipmentListResponse(page);
    }
    
    //장비 상세 조회
    public EquipmentResponse getEquipment(Long id) {
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        // 상세 정보로 변환하여 반환
        return new EquipmentResponse(equipment);
    }
    
    //장비대여요청
    @Transactional
    public RentalResponse createRentRequest(RentalRequest request) {
        // 현재 로그인한 사용자 조회
        Long userId = securityUtil.getCurrentUserId();
        // 장비 조회
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        Long equipmentId = request.getEquipmentId();
        // 장비가 대여 가능한지 확인
        if (equipment.getAvailable() != null && !equipment.getAvailable()) {
            throw new RuntimeException("현재 대여 불가능한 장비입니다.");
        }
        RentalStatus status = RentalStatus.RENTAL_PENDING;
        EquipmentRental rental = request.toEntity(userId, equipmentId, request.getRentalTime(), request.getReturnTime(), status);
        // 새로운 대여 신청 생성
        equipmentRentalRepository.save(rental);

        return new RentalResponse(rental);
    }

    //다중장비대여요청
    @Transactional
    public RentalListResponse createRentRequests(RentalListRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        
        List<RentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();
        
        LocalDateTime rentalTime = request.getStartTime();
        LocalDateTime returnTime = request.getEndTime();
        

        for (Long equipmentId : request.getEquipmentIds()) {
            try {
                // 장비 조회
                Equipment equipment = equipmentRepository.findById(equipmentId)
                        .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                
                // 장비가 대여 가능한지 확인
                if (equipment.getAvailable() != null && !equipment.getAvailable()) {
                    // 대여 불가능한 장비는 실패 목록에 추가
                    failedRequests.add(new FailedRentalInfo(equipmentId, "현재 대여 불가능한 장비입니다."));
                    continue;
                }
                
                RentalRequest singleRequest = new RentalRequest();
                RentalStatus status = RentalStatus.RENTAL_PENDING;
                EquipmentRental rental = singleRequest.toEntity(userId, equipmentId, rentalTime, returnTime, status);
                EquipmentRental savedRental = equipmentRentalRepository.save(rental);
                
                RentalResponse successResponse = new RentalResponse(savedRental);
                successResponses.add(successResponse);
                
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                failedRequests.add(new FailedRentalInfo(equipmentId, e.getMessage()));
            }
        }
        return new RentalListResponse(successResponses, failedRequests);
    }

    //단일장비반납
    //ReturnResponse 만든 이유는 반납예정시간이랑 실제반납시간이 다르기때문에에
    public RentalResponse createReturnRequest(RentalRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        LocalDateTime returnTime = LocalDateTime.now();
        RentalStatus status = RentalStatus.RETURN_PENDING;
        EquipmentRental equipmentRental = request.toEntity(userId, request.getEquipmentId(), request.getRentalTime(), returnTime, status);
        equipmentRentalRepository.save(equipmentRental);
        return new RentalResponse(equipmentRental);
    }

    //다중장비반납요청
    public RentalListResponse createReturnRequests(RentalListRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        
        List<RentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();

        LocalDateTime startTime = request.getStartTime();

        
        for (Long equipmentId : request.getEquipmentIds()) {
            try {
                // 장비 조회
                RentalRequest singleRequest = new RentalRequest();
                RentalStatus status = RentalStatus.RETURN_PENDING;
                LocalDateTime returnTime = request.getEndTime();
                EquipmentRental rental = singleRequest.toEntity(userId, equipmentId, startTime, returnTime, status);
                EquipmentRental savedRental = equipmentRentalRepository.save(rental);
                
                RentalResponse successResponse = new RentalResponse(savedRental);
                successResponses.add(successResponse);
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                //이건 메시지 뭐라해야할까?
                failedRequests.add(new FailedRentalInfo(equipmentId, e.getMessage()));
                continue;
            }
        }
        return new RentalListResponse(successResponses, failedRequests);
    }

    //대여/반납 요청 취소
    public void cancelRentalRequest(Long requestId) {
        equipmentRentalRepository.deleteById(requestId);
    }

    /**
     * 다중 대여/반납 요청 취소
     * @param userId 사용자 ID
     * @param cancelList 취소할 요청 ID 목록
     * @return 취소 성공한 요청 ID 목록
     */
    @Transactional
    public List<Long> cancelBulkRentalRequests(Long userId, CancelRequestList cancelList) {
        List<Long> canceledIds = new ArrayList<>();
        
        for (Long requestId : cancelList.getRequestIds()) {
            try {
                // 요청 조회
                EquipmentRental rental = equipmentRentalRepository.findById(requestId)
                        .orElseThrow(() -> new RuntimeException("요청을 찾을 수 없습니다."));
                
                // 본인의 요청인지 확인
                if (!rental.getUserId().equals(userId)) {
                    continue; // 본인의 요청이 아니면 건너뜀
                }
                
                // 취소 가능한 상태인지 확인 (PENDING 또는 RETURN_REQUESTED 상태만 취소 가능)
                RentalStatus status = rental.getStatus();
                if (status != RentalStatus.PENDING && status != RentalStatus.RETURN_REQUESTED) {
                    continue; // 취소 불가능한 상태이면 건너뜀
                }
                
                // 상태에 따른 처리
                if (status == RentalStatus.PENDING) {
                    // 대여 요청 취소의 경우 삭제
                    equipmentRentalRepository.delete(rental);
                } else {
                    // 반납 요청 취소의 경우 상태를 다시 APPROVED로 변경
                    rental.setStatus(RentalStatus.APPROVED);
                    equipmentRentalRepository.save(rental);
                }
                
                // 취소 성공한 ID 추가
                canceledIds.add(requestId);
            } catch (Exception e) {
                // 실패 시 다음 요청으로 넘어감
            }
        }
        
        return canceledIds;
    }

    //즐겨찾기 추가
    @Transactional
    public void addFavorite(Long userId, Long equipmentId) {
        // 이미 즐겨찾기에 있는지 확인
        if (!equipmentFavoriteRepository.existsByUserIdAndEquipmentId(userId, equipmentId)) {
            EquipmentFavorite favorite = EquipmentFavorite.builder()
                    .userId(userId)
                    .equipmentId(equipmentId)
                    .build();
            
            equipmentFavoriteRepository.save(favorite);
        }
    }
    
    //즐겨찾기 삭제
    @Transactional
    public void removeFavorite(Long userId, Long equipmentId) {
        equipmentFavoriteRepository.findByUserIdAndEquipmentId(userId, equipmentId)
                .ifPresent(equipmentFavoriteRepository::delete);
    }
}
