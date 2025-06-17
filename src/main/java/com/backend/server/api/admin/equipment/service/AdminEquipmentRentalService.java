package com.backend.server.api.admin.equipment.service;

import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentDetailRequest;
import com.backend.server.api.common.notification.dto.CommonNotificationDto;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.BrokenRepairHistory;
import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import com.backend.server.model.repository.history.BrokenRepairHistoryRepository;
import com.backend.server.model.repository.history.RentalHistoryRepository;
import java.util.List;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEquipmentRentalService {
    private final EquipmentRepository equipmentRepository;

    private final CommonNotificationService notificationService;
    private final BrokenRepairHistoryRepository brokenRepairHistoryRepository;
    private final RentalHistoryRepository rentalHistoryRepository;

    @Transactional
    public List<Long> changeStatus(AdminEquipmentDetailRequest request) {
        BiFunction<Long, String, Long> operator = switch (request.getStatus()) {
            case ACCEPT -> (l, s) -> rentalAccept(l); //장비대여승인
            case RETURN -> (l, s) -> rentalReturn(l);//반납
            case CANCEL -> this::rentalCancel;
            case BROKEN -> this::rentalBroken;//대
            case REJECT -> this::rentalReject;
        };
        for (Long classroomId : request.getIds())
            operator.apply(classroomId, request.getDetail());
        return request.getIds();
    }

    //승인
    public Long rentalAccept(Long equipmentId){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        String equipmentName = equipment.getEquipmentModel().getName();
        Long renterId = equipment.getRenter().getId();

        if(equipment.getStatus() != Status.RENTAL_PENDING){
            throw new IllegalArgumentException("대여 요청 중인 장비만 대여 가능합니다.");
        }

        equipment.makeInUse();
        equipmentRepository.save(equipment);

        RentalHistory rentalHistory = rentalHistoryRepository
                .findFirstByEquipmentAndRenterOrderByCreatedAtDesc(equipment, equipment.getRenter())
                .orElseThrow(() -> new IllegalArgumentException("대여 내역이 존재하지 않습니다."));
        rentalHistory.makeInUse();
        rentalHistoryRepository.save(rentalHistory);

        notificationProcess(equipmentId, equipmentName, renterId, "대여 승인", "");

        return equipmentId;
    }

    //반납
    public Long rentalReturn(Long equipmentId){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        String equipmentName = equipment.getEquipmentModel().getName();
        Long renterId = equipment.getRenter().getId();

        if(equipment.getStatus() != Status.IN_USE){
            throw new IllegalArgumentException("사용 중인 장비만 반납 가능합니다.");
        }

        RentalHistory rentalHistory = rentalHistoryRepository
                .findFirstByEquipmentAndRenterOrderByCreatedAtDesc(equipment, equipment.getRenter())
                .orElseThrow(() -> new IllegalArgumentException("대여 내역이 존재하지 않습니다."));
        rentalHistory.makeReturn();
        rentalHistoryRepository.save(rentalHistory);

        equipment.makeAvailable();
        equipmentRepository.save(equipment);

        notificationProcess(equipmentId, equipmentName, renterId, "대여 반납", "");

        return equipmentId;
    }

    //취소
    public Long rentalCancel(Long equipmentId, String detail){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        String equipmentName = equipment.getEquipmentModel().getName();
        Long renterId = equipment.getRenter().getId();

        if(equipment.getStatus() != Status.IN_USE){
            throw new IllegalArgumentException("대여 승인된 장비만 승인 취소가 가능합니다.");
        }

        RentalHistory rentalHistory = rentalHistoryRepository
                .findFirstByEquipmentAndRenterOrderByCreatedAtDesc(equipment, equipment.getRenter())
                .orElseThrow(() -> new IllegalArgumentException("대여 내역이 존재하지 않습니다."));
        rentalHistory.makeApprovalCancelled(detail);
        rentalHistoryRepository.save(rentalHistory);

        equipment.makeAvailable();
        equipmentRepository.save(equipment);

        notificationProcess(equipmentId, equipmentName, renterId, "대여 취소", "\n대여 취소 사유 : " + detail);

        return equipmentId;
    }

    //반려
    public Long rentalReject(Long equipmentId, String detail){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        String equipmentName = equipment.getEquipmentModel().getName();
        Long renterId = equipment.getRenter().getId();

        if(equipment.getStatus() != Status.RENTAL_PENDING){
            throw new IllegalArgumentException("대여 요청 상태인 장비만 반려 가능합니다.");
        }

        RentalHistory rentalHistory = rentalHistoryRepository
                .findFirstByEquipmentAndRenterOrderByCreatedAtDesc(equipment, equipment.getRenter())
                .orElseThrow(() -> new IllegalArgumentException("대여 내역이 존재하지 않습니다."));
        rentalHistory.makeRejected(detail);
        rentalHistoryRepository.save(rentalHistory);

        equipment.makeAvailable();
        equipmentRepository.save(equipment);

        notificationProcess(equipmentId, equipmentName, renterId, "대여 반려", "\n반려 사유 : " + detail);

        return equipmentId;
    }

    //파손처리
    public Long rentalBroken(Long equipmentId, String detail){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(()-> new IllegalArgumentException("장비를 찾을 수 없습니다"+equipmentId));
        String equipmentName = equipment.getEquipmentModel().getName();
        User renter = equipment.getRenter();
        Long renterId = equipment.getRenter().getId();

        if(equipment.getStatus() != Status.IN_USE){
            throw new IllegalArgumentException("대여 요청 상태인 장비만 반려 가능합니다.");
        }

        BrokenRepairHistory history = BrokenRepairHistory.markAsBrokenWhenEquipmentReturn(equipment, renter, detail);
        brokenRepairHistoryRepository.save(history);

        RentalHistory rentalHistory = rentalHistoryRepository
                .findFirstByEquipmentAndRenterOrderByCreatedAtDesc(equipment, equipment.getRenter())
                .orElseThrow(() -> new IllegalArgumentException("대여 내역이 존재하지 않습니다."));
        rentalHistory.makeBroken(history);
        rentalHistoryRepository.save(rentalHistory);

        equipment.makeBroken();
        equipmentRepository.save(equipment);

        notificationProcess(equipmentId, equipmentName, renterId, "반납시 파손처리", "\n파손 내용 : " + detail);

        return equipmentId;
    }

    public void notificationProcess(Long resourceId, String resourceName, Long renterId, String content, String extra) {
        // 대여자에게 알림 전송
        CommonNotificationDto notification = CommonNotificationDto.builder()
                .category("장비 %s".formatted(content))
                .title("장비 %s되었습니다.".formatted(content))
                .message("요청하신 장비 [%s] 가 %s되었습니다.%s"
                        .formatted(resourceName, content, extra))
                .link("/equipment/" + resourceId)
                .build();
        notificationService.createNotification(notification, renterId);
    }
}
