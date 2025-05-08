// package com.backend.server.api.admin.service;

// import com.backend.server.api.admin.equipment.dto.AdminManagerCandidatesResponse;
// import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListRequest;
// import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListResponse;
// import com.backend.server.api.admin.dto.equipment.PastAdminEquipmentCreateRequest;
// import com.backend.server.api.admin.dto.equipment.AdminEquipmentListRequest;
// import com.backend.server.api.admin.dto.equipment.AdminEquipmentListResponse;
// import com.backend.server.api.admin.dto.equipment.AdminEquipmentResponse;
// import com.backend.server.api.user.dto.equipment.EquipmentListRequest;
// import com.backend.server.model.entity.Equipment;
// import com.backend.server.model.entity.User;
// import com.backend.server.model.repository.EquipmentRepository;
// import com.backend.server.model.repository.EquipmentSpecification;
// import com.backend.server.model.repository.UserRepository;
// import lombok.RequiredArgsConstructor;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.ModelAttribute;

// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.function.Function;
// import java.util.stream.Collectors;

// import com.backend.server.model.entity.enums.Status;
// import com.backend.server.model.entity.enums.Role;
// import com.backend.server.model.entity.EquipmentRental;
// import com.backend.server.model.repository.EquipmentRentalRepository;



// @Service
// @RequiredArgsConstructor
// public class AdminEquipmentService {

//     private final EquipmentRepository equipmentRepository;
//     private final UserRepository userRepository;
//     private final EquipmentRentalRepository equipmentRentalRepository;

//     //장비등록할떄 관리자 목록 조회
//     public List<AdminManagerCandidatesResponse> getAdminUsers() {
//         List<User> adminUsers = userRepository.findByRoleIn(Role.ROLE_ADMIN,Role.ROLE_SUPER_ADMIN);
//         return adminUsers.stream()
//             .map(AdminManagerCandidatesResponse::new)
//             .collect(Collectors.toList());
//     }
//     //모든장비 조회 / 필터링
//     public AdminEquipmentListResponse getEquipments(AdminEquipmentListRequest request) {
//         Pageable pageable = EquipmentSpecification.getPageable(request);

//         Specification<Equipment> spec = EquipmentSpecification.AdminfilterEquipments(request);
//         Page<Equipment> page = equipmentRepository.findAll(spec, pageable);
//         return new AdminEquipmentListResponse(page);
//     }
//     //장비 상세 조회
//     public AdminEquipmentResponse getEquipment(Long id) {
//         Equipment equipment = equipmentRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
//         return new AdminEquipmentResponse(equipment);
//     }

//     //장비 등록
//     public void createEquipment(PastAdminEquipmentCreateRequest request) {
//         User manager = userRepository.findById(request.getManagerId())
//                 .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));
//         Equipment equipment = request.toEntity(manager, null);
//         equipmentRepository.save(equipment);
//     }

//     //장비 수정
//     public void updateEquipment(Long id, PastAdminEquipmentCreateRequest request) {
//         Equipment equipment = equipmentRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
//         User manager = userRepository.findById(request.getManagerId())
//                 .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));
//         equipment = request.toEntity(manager, equipment);
//         equipmentRepository.save(equipment);
//     }

//     //장비 삭제
//     public void deleteEquipment(Long id) {
//         equipmentRepository.deleteById(id);
//     }

//     //장비 대여/반납 요청 목록 조회 (status를 다르게해서 대여요청 목록과 반납요청 목록 둘다 조회 가능)
//     public AdminEquipmentRentalRequestListResponse getRentalRequests(AdminEquipmentRentalRequestListRequest request) {
//         Pageable pageable = EquipmentSpecification.getRentalRequestPageable(request);
//         Specification<EquipmentRental> spec = EquipmentSpecification.filterRentalRequests(request);
    
//         // 1. 대여 내역 페이지네이션
//         Page<EquipmentRental> page = equipmentRentalRepository.findAll(spec, pageable);
    
//         // 2. 유저랑 장비 ID 가져옴
//         List<Long> userIds = page.getContent().stream()
//             .map(EquipmentRental::getUserId)
//             .distinct()
//             .toList();
    
//         List<Long> equipmentIds = page.getContent().stream()
//             .map(EquipmentRental::getEquipmentId)
//             .distinct()
//             .toList();
    
//         // 3. 유저와 장비를 한 번에 조회하고 맵으로 전환ㅇ..... 이거 너무 어렵다
//         Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
//             .collect(Collectors.toMap(User::getId, Function.identity()));
//             //User::getId, Function.identity() 이거는
//             //유저 아이디가 키값, 그리고 그 아이디에 맞는 유저 정보. 유저 객체가 벨류임.
    
//         Map<Long, Equipment> equipmentMap = equipmentRepository.findAllById(equipmentIds).stream()
//             .collect(Collectors.toMap(Equipment::getId, Function.identity()));
    
//         // 4.위 과정 조립
//         return new AdminEquipmentRentalRequestListResponse(page, userMap, equipmentMap);
//     }

//     //장비 대여 요청 다중 승인
//     @Transactional
//     public void approveRentalRequests(List<Long> ids) {
//         List<EquipmentRental> rentals = equipmentRentalRepository.findAllById(ids);
//         for (EquipmentRental rental : rentals) {
//             rental.approveRental();
//             // 장비 상태도 함께 업데이트
//             Equipment equipment = equipmentRepository.findByIdForUpdate(rental.getEquipmentId())
//                 .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));
            
//             // 현재 장비의 수량 감소
//             equipment.setQuantity(equipment.getQuantity() - rental.getQuantity());
//             equipmentRepository.save(equipment);

//             //요청이 승인됐으니까 장비 상태가 바뀐 채로 row하나 더만들기
//             Equipment rentalPendingEquipment = equipment.toBuilder()
//                 .id(null)
//                 .quantity(rental.getQuantity())
//                 .rentalStatus(Status.IN_USE)
//                 .available(false)
//                 .build();
//             equipmentRepository.save(rentalPendingEquipment);
//         }
//         // 대여 요청 레코드 삭제
//         equipmentRentalRepository.deleteAll(rentals);
//     }

//     @Transactional
//     public void rejectRentalRequests(List<Long> ids) {
//         List<EquipmentRental> rentals = equipmentRentalRepository.findAllById(ids);

//         for (EquipmentRental rental : rentals) {
//             // 1. 기존 장비 (원본 row) 찾기
//             Equipment originalEquipment = equipmentRepository.findByIdForUpdate(rental.getEquipmentId())
//                 .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));

//             // 2. rentalPendingEquipment 삭제 (id=null로 생성된 것)
//             List<Equipment> duplicates = equipmentRepository.findByRentalStatusAndName(
//                 Status.RENTAL_PENDING, originalEquipment.getName());

//             for (Equipment duplicate : duplicates) {
//                 if (!duplicate.getId().equals(originalEquipment.getId())) {
//                     equipmentRepository.delete(duplicate); // 임시 장비 row 삭제
//                 }
//             }

//             // 3. 원본 장비 수량 복구
//             originalEquipment.setQuantity(originalEquipment.getQuantity() + rental.getQuantity());
//             equipmentRepository.save(originalEquipment);
//         }

//         // 4. 대여 요청 삭제
//         equipmentRentalRepository.deleteAll(rentals);
//     }

//     @Transactional
// public void approveReturnRequestsNormal(List<Long> ids) {
//     List<EquipmentRental> returns = equipmentRentalRepository.findAllById(ids);

//     for (EquipmentRental rental : returns) {
//         // 1. 기존 장비 찾기
//         Equipment originalEquipment = equipmentRepository.findByIdForUpdate(rental.getEquipmentId())
//             .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));

//         // 2. rentalPending 상태의 임시 장비 삭제
//         List<Equipment> duplicates = equipmentRepository.findByRentalStatusAndName(
//             Status.RETURN_PENDING, originalEquipment.getName());
        
//         for (Equipment duplicate : duplicates) {
//             if (!duplicate.getId().equals(originalEquipment.getId())) {
//                 equipmentRepository.delete(duplicate); // 임시 장비 row 삭제
//             }
//         }

//         // 3. 원래 장비 수량 복원
//         originalEquipment.setQuantity(originalEquipment.getQuantity() + rental.getQuantity());
//         originalEquipment.setRentalStatus(Status.AVAILABLE);
//         equipmentRepository.save(originalEquipment);
//     }

//     // 4. 반납 요청 삭제
//     equipmentRentalRepository.deleteAll(returns);
// }


// @Transactional
// public void approveReturnDamegedRequestsNormal(List<Long> ids) {
//     List<EquipmentRental> returns = equipmentRentalRepository.findAllById(ids);

//     for (EquipmentRental rental : returns) {
//         // 1. 기존 장비 찾기
//         Equipment originalEquipment = equipmentRepository.findByIdForUpdate(rental.getEquipmentId())
//             .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다."));

//         // 2. RETURN_PENDING 임시 장비 삭제
//         List<Equipment> duplicates = equipmentRepository.findByRentalStatusAndName(
//             Status.RETURN_PENDING, originalEquipment.getName());

//         for (Equipment duplicate : duplicates) {
//             if (!duplicate.getId().equals(originalEquipment.getId())) {
//                 equipmentRepository.delete(duplicate); // 임시 장비 row 삭제
//             }
//         }

//         // 3. 원래 장비 수량 감소 or 파손 처리
//         // 수량 감소하지 않고 상태만 BROKEN 처리할 경우:
//         originalEquipment.setRentalStatus(Status.BROKEN);
//         equipmentRepository.save(originalEquipment);
//     }

//     // 4. 반납 요청 삭제
//     equipmentRentalRepository.deleteAll(returns);
// }

    
// } 