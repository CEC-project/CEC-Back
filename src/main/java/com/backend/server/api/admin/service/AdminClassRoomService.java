package com.backend.server.api.admin.service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.server.api.admin.dto.classroom.AdminClassRoomCreateRequest;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomIdResponse;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListRequest;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListResponse;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomResponse;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomRentalRequestListRequest;
import com.backend.server.api.admin.dto.classroom.AdminClassRoomRentalRequestListResponse;
import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RentalStatus;
import com.backend.server.model.repository.ClassRoomRespository;
import com.backend.server.model.repository.ClassRoomSpecification;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.repository.ClassRoomRentalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminClassRoomService {

    private ClassRoomRespository classRoomRespository;  
    private UserRepository userRepository;
    private final ClassRoomRentalRepository classRoomRentalRepository;
    
    //강의실 등록
    public AdminClassRoomIdResponse createClassRoom(AdminClassRoomCreateRequest request){
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));
        ClassRoom classRoom = request.toEntity(manager, null);
        classRoomRespository.save(classRoom);
        return new AdminClassRoomIdResponse(classRoom.getId());
    }

    //강의실 수정
    public void updateClassRoom(Long id, AdminClassRoomCreateRequest request){
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        classRoom = request.toEntity(manager, classRoom);
        classRoomRespository.save(classRoom);
    }

    //강의실 삭제
    public void deleteClassRoom(Long id){
        classRoomRespository.deleteById(id);
    }

    //강의실 목록조회
    public CommonClassRoomListResponse getClassRooms(CommonClassRoomListRequest request){
        Pageable pageable = ClassRoomSpecification.getPageable(request);
        Specification<ClassRoom> spec = ClassRoomSpecification.filterClassRooms(request);
        Page<ClassRoom> page = classRoomRespository.findAll(spec, pageable);
        return new CommonClassRoomListResponse(page);
    }

    //강의실  상세 조회
    public CommonClassRoomResponse getClassRoom(Long id){
        ClassRoom classRoom = classRoomRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        return new CommonClassRoomResponse(classRoom);
    }

    @Transactional(readOnly = true)
    //장비 대여/반납 요청 목록 조회 (status를 다르게해서 대여요청 목록과 반납요청 목록 둘다 조회 가능)
    public AdminClassRoomRentalRequestListResponse getRentalRequests(AdminClassRoomRentalRequestListRequest request) {
        Pageable pageable = ClassRoomSpecification.getPageable(request);
        Specification<ClassRoomRental> spec = ClassRoomSpecification.filterRentalRequests(request);
    
        // 1. 대여 내역 페이지네이션
        Page<ClassRoomRental> page = classRoomRentalRepository.findAll(spec, pageable);
    
        // 2. 유저랑 장비 ID 가져옴
        List<Long> userIds = page.getContent().stream()
            .map(ClassRoomRental::getRenterId)
            .distinct()
            .toList();
    
        List<Long> equipmentIds = page.getContent().stream()
            .map(ClassRoomRental::getClassRoomId)
            .distinct()
            .toList();
    
        // 3. 유저와 장비를 한 번에 조회하고 맵으로 전환ㅇ..... 이거 너무 어렵다
        Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));
            //User::getId, Function.identity() 이거는
            //유저 아이디가 키값, 그리고 그 아이디에 맞는 유저 정보. 유저 객체가 벨류임.
    
        Map<Long, ClassRoom> classRoomMap = classRoomRespository.findAllById(equipmentIds).stream()
            .collect(Collectors.toMap(ClassRoom::getId, Function.identity()));
    
        // 4.위 과정 조립
        return new AdminClassRoomRentalRequestListResponse(page, userMap, classRoomMap);
    }

    @Transactional
    public void approveRentalRequest(Long rentalId) {
        ClassRoomRental rental = classRoomRentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("대여 요청을 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(rental.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        classRoom.setRentalStatus(RentalStatus.IN_USE);
        classRoomRespository.save(classRoom);
        classRoomRentalRepository.save(rental);
    }

    @Transactional
    public void rejectRentalRequest(Long rentalId) {
        ClassRoomRental rental = classRoomRentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("대여 요청을 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(rental.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        classRoom.setRentalStatus(RentalStatus.AVAILABLE);
        classRoomRespository.save(classRoom);
        classRoomRentalRepository.delete(rental);
    }

    @Transactional
    public void approveReturnRequest(Long rentalId) {
        ClassRoomRental rental = classRoomRentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("대여 요청을 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(rental.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        classRoom.setRentalStatus(RentalStatus.AVAILABLE);
        classRoomRespository.save(classRoom);
        classRoomRentalRepository.delete(rental);
    }

    @Transactional
    public void approveReturnRequestDamaged(Long rentalId) {
        ClassRoomRental rental = classRoomRentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("대여 요청을 찾을 수 없습니다."));
        ClassRoom classRoom = classRoomRespository.findById(rental.getClassRoomId())
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));
        classRoom.setRentalStatus(RentalStatus.BROKEN);
        classRoomRespository.save(classRoom);
        classRoomRentalRepository.delete(rental);
    }
}
                                                    