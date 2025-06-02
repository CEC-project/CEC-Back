package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentDetailRequest;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.enums.BrokenType;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentBrokenHistory;
import com.backend.server.model.repository.equipment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class AdminEquipmentRentalService {
    private final EquipmentRepository equipmentRepository;

    private final CommonNotificationService notificationService;
    private final EquipmentBrokenHistoryRepository equipmentBrokenHistoryRepository;

    @Transactional
    public List<Long> changeStatus(AdminEquipmentDetailRequest request) {
        BiFunction<Long, String, Long> operator = switch (request.getStatus()) {
            case ACCEPT -> (l, s) -> rentalAccept(l); //장비대여승인
            case RETURN -> (l, s) -> rentalReturn(l);//반납
            //case CANCEL -> this::rentalCancel;
            case BROKEN -> this::rentalBroken;//대
            case REJECT -> this::rentalReject;
        };
        for (Long classroomId : request.getEquipmentIds())
            operator.apply(classroomId, request.getDetail());
        return request.getEquipmentIds();
    }
    //승인
    public Long rentalAccept(Long equipmentId){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));

        if(equipment.getStatus() != Status.RENTAL_PENDING){
            throw new IllegalArgumentException("대여 요청 중인 장비만 대여 가능합니다.");
        }
        equipment = equipment.toBuilder()
                .status(Status.IN_USE).build();
        equipmentRepository.save(equipment);

        notificationProcess(equipment, "대여 승인", "");


        return equipmentId;
    }
    //반납
    public Long rentalReturn(Long equipmentId){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));

        if(equipment.getStatus() != Status.IN_USE){
            throw new IllegalArgumentException("사용 중인 장비만 반납 가능합니다.");
        }
        equipment = equipment.toBuilder()
                .status(Status.AVAILABLE)
                .renter(null)
                .startRentDate(null)
                .endRentDate(null)
                .build();

        equipmentRepository.save(equipment);

        notificationProcess(equipment, "대여 반납", "");

        return equipmentId;
    }
    //반려
    public Long rentalReject(Long equipmentId, String detail){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));

        if(equipment.getStatus() != Status.RENTAL_PENDING){
            throw new IllegalArgumentException("대여 요청 상태인 장비만 반려 가능합니다.");
        }
        equipment = equipment.toBuilder()
                .status(Status.AVAILABLE)
                .renter(null)
                .startRentDate(null)
                .endRentDate(null)
                .build();

        equipmentRepository.save(equipment);

        notificationProcess(equipment, "대여 반려", "\n반려 사유 : " + detail);

        return equipmentId;
    }

    //파손처리
    public Long rentalBroken(Long equipmentId, String detail){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));

        if(equipment.getStatus() != Status.IN_USE){
            throw new IllegalArgumentException("대여 요청 상태인 장비만 반려 가능합니다.");
        }

        EquipmentBrokenHistory history = EquipmentBrokenHistory.builder()
                .equipment(equipment)
                .brokenBy(equipment.getRenter())
                .brokenType(BrokenType.RETURN_BROKEN)
                .brokenDetail(detail)
                .build();

        equipmentBrokenHistoryRepository.save(history);

        equipment = equipment.toBuilder()
                .status(Status.BROKEN)
                .renter(null)
                .startRentDate(null)
                .endRentDate(null)
                .build();

        equipmentRepository.save(equipment);

        notificationProcess(equipment, "반납시 파손처리", "\n파손 내용 : " + detail);

        return equipmentId;
    }

    @Transactional
    public void extendRentalPeriods(List<Long> equipmentIds, LocalDateTime newEndDate) {
        if (newEndDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("연장 날짜는 현재 시간보다 이후여야 합니다.");
        }

        for (Long equipmentId : equipmentIds) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new RuntimeException("장비를 찾을 수 없습니다. ID: " + equipmentId));

            if (Status.IN_USE != equipment.getStatus()) {
                throw new IllegalStateException("대여 중인 장비만 기간을 연장할 수 있습니다. ID: " + equipmentId);
            }

            Equipment updated = equipment.toBuilder()
                    .endRentDate(newEndDate)
                    .build();

            equipmentRepository.save(updated);
        }
    }


    public void notificationProcess(Equipment equipment, String content, String extra) {
        Long classroomId = equipment.getId();
        Long renterId = equipment.getRenter().getId();

        // 대여자에게 알림 전송
        CommonNotificationDto notification = CommonNotificationDto.builder()
                .category("장비 %s".formatted(content))
                .title("장비 %s되었습니다.".formatted(content))
                .message("요청하신 장비 [%s] 가 %s되었습니다.%s"
                        .formatted(equipment.getEquipmentModel().getName(), content, extra))
                .link("/equipment/" + classroomId)
                .build();
        notificationService.createNotification(notification, renterId);
    }
}
