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

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.dto.equipment.EquipmentListRequest;
import com.backend.server.api.user.dto.equipment.EquipmentListResponse;
import com.backend.server.api.user.dto.equipment.EquipmentRentalItem;
import com.backend.server.api.user.dto.equipment.EquipmentRentalResponse;
import com.backend.server.api.user.dto.equipment.EquipmentResponse;
import com.backend.server.api.user.dto.equipment.FavoriteListResponse;
import com.backend.server.api.user.dto.equipment.EquipmentRentalListRequest;
import com.backend.server.api.user.dto.equipment.EquipmentRentalListResponse;
import com.backend.server.api.user.dto.equipment.EquipmentRentalRequest;
import com.backend.server.api.user.dto.equipment.EquipmentRentalResponse;
import com.backend.server.api.user.dto.equipment.EquipmentRentalListResponse.FailedRentalInfo;
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
//import com.backend.server.security.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentRentalRepository equipmentRentalRepository;
    private final UserRepository userRepository;
    //private final SecurityUtil securityUtil;
    private final EquipmentFavoriteRepository equipmentFavoriteRepository;
 

    //장비 목록 조회
    public EquipmentListResponse getEquipments(LoginUser loginUser, EquipmentListRequest request) {

        Pageable pageable = EquipmentSpecification.getPageable(request);
        //유저학년찾기
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Integer grade = user.getGrade();
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
    public EquipmentRentalResponse createRentRequest(LoginUser loginUser, EquipmentRentalRequest request) {
        // 현재 로그인한 사용자 조회
        Long userId = loginUser.getId();
        // 장비 조회
        Equipment equipment = equipmentRepository.findByIdForUpdate(request.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        Long equipmentId = request.getEquipmentId();
        // 장비가 대여 가능한지 확인
        if (equipment.getAvailable() != null && !equipment.getAvailable()) {
            throw new RuntimeException("현재 대여 불가능한 장비입니다.");
        }
        if (equipment.getMaxRentalCount() != null && equipment.getMaxRentalCount() <= request.getQuantity()) {
            throw new RuntimeException("대여 가능한 수량을 초과했습니다.");
        }
        RentalStatus status = RentalStatus.RENTAL_PENDING;
        EquipmentRental rental = request.toEntity(userId, equipmentId, request.getRentalTime(), request.getReturnTime(), status, request.getQuantity());
        // 새로운 대여 신청 생성
        equipmentRentalRepository.save(rental);
        equipment.setQuantity(equipment.getQuantity() - request.getQuantity());
        equipmentRepository.save(equipment);    
        Equipment rentalPendingEquipment = equipment.toBuilder()
        .id(null)
        .quantity(request.getQuantity())
        .rentalStatus(status)
        .available(false)
        .build();
        equipmentRepository.save(rentalPendingEquipment);

        return new EquipmentRentalResponse(rental);
    }

    //다중장비대여요청
    @Transactional
    public EquipmentRentalListResponse createRentRequests(LoginUser loginUser, EquipmentRentalListRequest request) {
        Long userId = loginUser.getId();
        
        List<EquipmentRentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();
        
        LocalDateTime rentalTime = request.getStartTime();
        LocalDateTime returnTime = request.getEndTime();
        

        for (EquipmentRentalItem item : request.getItems()) {

            try {
                // 장비 조회
                Equipment equipment = equipmentRepository.findByIdForUpdate(item.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                
                // 장비가 대여 가능한지 확인
                if (equipment.getAvailable() != null && !equipment.getAvailable()) {
                    // 대여 불가능한 장비는 실패 목록에 추가
                    failedRequests.add(new FailedRentalInfo(item.getEquipmentId(), "현재 대여 불가능한 장비입니다."));
                    continue;
                }
                
                EquipmentRentalRequest singleRequest = new EquipmentRentalRequest();
                RentalStatus status = RentalStatus.RENTAL_PENDING;
                EquipmentRental rental = singleRequest.toEntity(userId, item.getEquipmentId(), rentalTime, returnTime, status, item.getQuantity());
                EquipmentRental savedRental = equipmentRentalRepository.save(rental);
                equipment.setQuantity(equipment.getQuantity() - item.getQuantity());
                equipmentRepository.save(equipment);
                
                EquipmentRentalResponse successResponse = new EquipmentRentalResponse(savedRental);
                successResponses.add(successResponse);

                Equipment rentalPendingEquipment = equipment.toBuilder()
                    .id(null)
                    .quantity(item.getQuantity())
                    .rentalStatus(status)
                    .available(false)
                .build();
                equipmentRepository.save(rentalPendingEquipment);
                
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                failedRequests.add(new FailedRentalInfo(item.getEquipmentId(), e.getMessage()));
            }
        }
        
        return new EquipmentRentalListResponse(successResponses, failedRequests);
    }


    //다중장비반납요청
    public EquipmentRentalListResponse createReturnRequests(LoginUser loginUser, EquipmentRentalListRequest request) {
        Long userId = loginUser.getId();
        
        List<EquipmentRentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();

        LocalDateTime startTime = request.getStartTime();

        
        for (EquipmentRentalItem item : request.getItems()) {
            try {
                // 장비 조회
                EquipmentRentalRequest singleRequest = new EquipmentRentalRequest();
                RentalStatus status = RentalStatus.RETURN_PENDING;
                LocalDateTime returnTime = request.getEndTime();
                EquipmentRental rental = singleRequest.toEntity(userId, item.getEquipmentId(), startTime, returnTime, status, item.getQuantity());
                EquipmentRental savedRental = equipmentRentalRepository.save(rental);
                Equipment equipment = equipmentRepository.findById(item.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                equipment.toBuilder().rentalStatus(status).build();
                equipmentRepository.save(equipment);
                EquipmentRentalResponse successResponse = new EquipmentRentalResponse(savedRental);
                successResponses.add(successResponse);
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                //이건 메시지 뭐라해야할까?
                failedRequests.add(new FailedRentalInfo(item.getEquipmentId(), e.getMessage()));
                continue;
            }
        }
        return new EquipmentRentalListResponse(successResponses, failedRequests);
    }

    // //단일장비반납
    // //ReturnResponse 만든 이유는 반납예정시간이랑 실제반납시간이 다르기때문에에
    // public EquipmentRentalResponse createReturnRequest(LoginUser loginUser, EquipmentRentalRequest request) {
    //     Long userId = loginUser.getId();
    //     LocalDateTime returnTime = LocalDateTime.now();
    //     RentalStatus status = RentalStatus.RETURN_PENDING;
    //     EquipmentRental equipmentRental = request.toEntity(userId, request.getEquipmentId(), request.getRentalTime(), returnTime, status, request.getQuantity());
    //     equipmentRentalRepository.save(equipmentRental);
    //     Equipment equipment = equipmentRepository.findByIdForUpdate(request.getEquipmentId())
    //     .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
    //     if

        
    //     equipment.setQuantity(equipment.getQuantity() + request.getQuantity());
    //     return new EquipmentRentalResponse(equipmentRental);
    // }

    //대여/반납 요청 취소
    public void cancelRentalRequest(Long requestId) {
        equipmentRentalRepository.deleteById(requestId);
    }

    //다중 대여/반납 요청 취소
    public void cancelBulkRentalRequests(List<Long> requestIds) {
        equipmentRentalRepository.deleteAllById(requestIds);
    }

    //즐찾추가
    public void addFavorite(Long equipmentId, LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        EquipmentFavorite favorite = EquipmentFavorite.builder()
                .userId(user.getId())
                .equipmentId(equipment.getId())
                .build();
        equipmentFavoriteRepository.save(favorite);
    }

    //즐찾삭제
    public void removeFavorite(Long equipmentId, LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        equipmentFavoriteRepository.deleteByUserIdAndEquipmentId(user.getId(), equipment.getId());
    }
    
    //즐찾목록조호 (페이지네이션)
    public FavoriteListResponse getFavoriteList(LoginUser loginUser, EquipmentListRequest request) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Pageable pageable = EquipmentSpecification.getPageable(request);
        
        List<Long> favoriteEquipmentIds = equipmentFavoriteRepository.findByUserId(user.getId())
                .stream()
                .map(EquipmentFavorite::getEquipmentId)
                .collect(Collectors.toList());
        
        Specification<Equipment> spec = (root, query, criteriaBuilder) -> 
            root.get("id").in(favoriteEquipmentIds);
        
        if (request.getCategoryId() != null) {
            spec = spec.and(EquipmentSpecification.filterEquipments(request, user.getGrade()));
        }
        
        Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
        
        return new FavoriteListResponse(page);
    }

   
}
