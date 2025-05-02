package com.backend.server.api.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListRequest;
import com.backend.server.api.user.dto.classroom.ClassRoomListResponse;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalListRequest;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalListResponse;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalListResponse.FailedRentalInfo;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalRequest;
import com.backend.server.api.user.dto.classroom.ClassRoomRentalResponse;
import com.backend.server.api.user.dto.classroom.ClassRoomResponse;
import com.backend.server.api.user.dto.classroom.FavoriteListResponse;
import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.ClassRoomFavorite;
import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.ClassRoomFavoriteRepository;
import com.backend.server.model.repository.ClassRoomRentalRepository;
import com.backend.server.model.repository.ClassRoomRespository;
import com.backend.server.model.repository.ClassRoomSpecification;
import com.backend.server.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassRoomService {
    private final ClassRoomRespository classRoomRespository;
    private final ClassRoomRentalRepository classRoomRentalRepository;
    private final ClassRoomFavoriteRepository classRoomFavoriteRepository;
    private final UserRepository userRepository;

    //강의실 목록 조회
    public ClassRoomListResponse getClassRooms(CommonClassRoomListRequest request) {
        Pageable pageable = ClassRoomSpecification.getPageable(request);
        Specification<ClassRoom> spec = ClassRoomSpecification.filterClassRooms(request);
        Page<ClassRoom> page = classRoomRespository.findAll(spec, pageable);
        return new ClassRoomListResponse(page);
    }

    //강의실 상세 조회
    @Transactional(readOnly = true)
    public ClassRoomResponse getClassRoom(Long id) {
        ClassRoom classRoom = classRoomRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        return new ClassRoomResponse(classRoom);
    }

    //강의실 대여 요청청
    @Transactional
    public ClassRoomRentalResponse createRentRequest(LoginUser loginUser, ClassRoomRentalRequest request) {
        // 현재 로그인한 사용자 조회
        Long userId = loginUser.getId();
        // 장비 조회
        ClassRoom classRoom = classRoomRespository.findById(request.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        
        Long classRoomId = request.getClassRoomId();
        // 장비가 대여 가능한지 확인
        if (classRoom.getAvailable() != null && !classRoom.getAvailable()) {
            throw new RuntimeException("현재 대여 불가능한 장비입니다.");
        }
        Status status = Status.RENTAL_PENDING;
        ClassRoomRental rental = request.toEntity(userId, classRoomId, request.getRentalTime(), request.getReturnTime(), status);
        // 새로운 대여 신청 생성
        classRoomRentalRepository.save(rental);

        return new ClassRoomRentalResponse(rental);
    }

    //다중장비대여요청
    @Transactional
    public ClassRoomRentalListResponse createRentRequests(LoginUser loginUser, ClassRoomRentalListRequest request) {
        Long userId = loginUser.getId();
        
        List<ClassRoomRentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();
        
        LocalDateTime rentalTime = request.getStartTime();
        LocalDateTime returnTime = request.getEndTime();
        

        for (Long classRoomId : request.getClassRoomIds()) {
            try {
                // 장비 조회
                ClassRoom classRoom = classRoomRespository.findById(classRoomId)
                        .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
                
                // 장비가 대여 가능한지 확인
                if (classRoom.getAvailable() != null && !classRoom.getAvailable()) {
                    // 대여 불가능한 장비는 실패 목록에 추가
                    failedRequests.add(new FailedRentalInfo(classRoomId, "현재 대여 불가능한 장비입니다."));
                    continue;
                }
                
                ClassRoomRentalRequest singleRequest = new ClassRoomRentalRequest();
                Status status = Status.RENTAL_PENDING;
                ClassRoomRental rental = singleRequest.toEntity(userId, classRoomId, rentalTime, returnTime, status);
                ClassRoomRental savedRental = classRoomRentalRepository.save(rental);
                
                ClassRoomRentalResponse successResponse = new ClassRoomRentalResponse(savedRental);
                successResponses.add(successResponse);
                
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                failedRequests.add(new FailedRentalInfo(classRoomId, e.getMessage()));
            }
        }
        return new ClassRoomRentalListResponse(successResponses, failedRequests);
    }

    //단일장비반납
    @Transactional
    public ClassRoomRentalResponse createReturnRequest(LoginUser loginUser, ClassRoomRentalRequest request) {
        Long userId = loginUser.getId();
        LocalDateTime returnTime = LocalDateTime.now();
        Status status = Status.RETURN_PENDING;
        ClassRoomRental equipmentRental = request.toEntity(userId, request.getClassRoomId(), request.getRentalTime(), returnTime, status);
        classRoomRentalRepository.save(equipmentRental);
        return new ClassRoomRentalResponse(equipmentRental);
    }

    //다중장비반납요청
    @Transactional
    public ClassRoomRentalListResponse createReturnRequests(LoginUser loginUser, ClassRoomRentalListRequest request) {
        Long userId = loginUser.getId();
        
        List<ClassRoomRentalResponse> successResponses = new ArrayList<>();
        List<FailedRentalInfo> failedRequests = new ArrayList<>();

        LocalDateTime startTime = request.getStartTime();

        
        for (Long classRoomId : request.getClassRoomIds()) {
            try {
                // 장비 조회
                ClassRoomRentalRequest singleRequest = new ClassRoomRentalRequest();
                Status status = Status.RETURN_PENDING;
                LocalDateTime returnTime = request.getEndTime();
                ClassRoomRental rental = singleRequest.toEntity(userId, classRoomId, startTime, returnTime, status);
                ClassRoomRental savedRental = classRoomRentalRepository.save(rental);
                
                ClassRoomRentalResponse successResponse = new ClassRoomRentalResponse(savedRental);
                successResponses.add(successResponse);
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                //이건 메시지 뭐라해야할까?
                failedRequests.add(new FailedRentalInfo(classRoomId, e.getMessage()));
                continue;
            }
        }
        return new ClassRoomRentalListResponse(successResponses, failedRequests);
    }

    //대여/반납 요청 취소
    @Transactional
    public void cancelRentalRequest(Long requestId) {
        classRoomRentalRepository.deleteById(requestId);
    }

    //다중 대여/반납 요청 취소
    @Transactional
    public void cancelBulkRentalRequests(List<Long> requestIds) {
        classRoomRentalRepository.deleteAllById(requestIds);
    }

    //즐찾추가
    @Transactional
    public void addFavorite(Long equipmentId, LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        ClassRoomFavorite favorite = ClassRoomFavorite.builder()
                .userId(user.getId())
                .classRoomId(classRoom.getId())
                .build();
        classRoomFavoriteRepository.save(favorite);
    }

    //즐찾삭제
    @Transactional
    public void removeFavorite(Long classRoomId, LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(classRoomId)
                .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
        classRoomFavoriteRepository.deleteByUserIdAndClassRoomId(user.getId(), classRoom.getId());
    }
    
    //즐찾목록조호 (페이지네이션)
    @Transactional(readOnly = true)
    public FavoriteListResponse getFavoriteList(LoginUser loginUser, CommonClassRoomListRequest request) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Pageable pageable = ClassRoomSpecification.getPageable(request);
        
        List<Long> favoriteClassRoomIds = classRoomFavoriteRepository.findByUserId(user.getId())
                .stream()
                .map(ClassRoomFavorite::getClassRoomId)
                .collect(Collectors.toList());
        
        Specification<ClassRoom> spec = (root, query, criteriaBuilder) -> 
            root.get("id").in(favoriteClassRoomIds);
        
        
        Page<ClassRoom> page = classRoomRespository.findAll(spec, pageable);
        
        return new FavoriteListResponse(page);
    }
} 