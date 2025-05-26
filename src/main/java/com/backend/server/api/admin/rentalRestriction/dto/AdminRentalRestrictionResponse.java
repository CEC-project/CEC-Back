package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RestrictionReason;
import com.backend.server.model.entity.enums.RestrictionType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AdminRentalRestrictionResponse {

    //ID 학년 성별 학번 이름

    private final Long id;
    private final Integer grade;
    private final String gender;
    private final String studentNumber;
    private final String name;

    //제재유형 제재기간 제재사유 제재처리시간

    private final RestrictionType type;
    private final RestrictionReason reason;
    private final LocalDateTime endAt;
    private final LocalDateTime startAt;

    public AdminRentalRestrictionResponse(User user, RentalRestriction rentalRestriction) {
        id = rentalRestriction.getId();
        grade = user.getGrade();
        gender = user.getGender();
        studentNumber = user.getStudentNumber();
        name = user.getName();

        type = rentalRestriction.getType();
        reason = rentalRestriction.getReason();
        endAt = rentalRestriction.getEndAt();
        startAt = rentalRestriction.getCreatedAt();
    }
}