package com.backend.server.fixture;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.classroom.SemesterSchedule;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public enum EquipmentFixture {

    장비1(
            "SERIAL-0001",
            "카메라",
            "2", // restrictionGrade
            "https://example.com/image1.jpg",
            Status.AVAILABLE,
            5L,  // rentalCount
            1L,  // brokenCount
            2L,  // repairCount
            null, // managerId
            null, // renter
            null, // semesterSchedule
            null, // requestedTime
            null, // startRentTime
            null  // endRentTime
    ),

    장비2(
            "SERIAL-0002",
            "삼1각대",
            "3",
            "https://example.com/image2.jpg",
            Status.IN_USE,
            10L,
            0L,
            1L,
            null,
            null,    //테스트 코드 짤때 넣어야함
            null,
            LocalDateTime.now().minusDays(1),   // requestedTime
            LocalDateTime.now().minusDays(1),   // startRentTime
            LocalDateTime.now().plusDays(1)     // endRentTime
    );

    private final String serialNumber;
    private final String description;
    private final String restrictionGrade;
    private final String imageUrl;
    private final Status status;
    private final Long rentalCount;
    private final Long brokenCount;
    private final Long repairCount;
    private final Long managerId;
    private final User renter;
    private final SemesterSchedule semesterSchedule;
    private final LocalDateTime requestedTime;
    private final LocalDateTime startRentTime;
    private final LocalDateTime endRentTime;

    public Equipment 엔티티_생성(
            EquipmentCategory category,
            EquipmentModel model
    ) {
        return Equipment.builder()
                .equipmentCategory(category)
                .equipmentModel(model)
                .serialNumber(serialNumber)
                .description(description)
                .restrictionGrade(restrictionGrade)
                .imageUrl(imageUrl)
                .status(status)
                .rentalCount(rentalCount)
                .brokenCount(brokenCount)
                .repairCount(repairCount)
                .managerId(managerId)
                .renter(renter)
                .semesterSchedule(semesterSchedule)
                .requestedTime(requestedTime)
                .startRentTime(startRentTime)
                .endRentTime(endRentTime)
                .build();
    }
}
