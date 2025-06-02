package com.backend.server.api.admin.user.dto;

import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminUserResponse {
    private Long id;
    private String name;
    private String studentNumber;
    private String nickname;
    private Integer year;
    private Gender gender;
    private String professor;
    private String phoneNumber;
    private String email;
    private String profilePicture;
    private LocalDate birthDate;
    private int rentalCount;
    private int brokenCount;
    private int restrictionCount;
    private int reportCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public AdminUserResponse(User user, Professor professor) {
        id = user.getId();
        studentNumber = user.getStudentNumber();
        name = user.getName();
        nickname = user.getNickname();
        year = user.getGrade();
        gender = user.getGender();
        this.professor = professor == null ? null : professor.getName();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        profilePicture = user.getProfilePicture();
        birthDate = user.getBirthDate();
        rentalCount = user.getRentalCount();
        brokenCount = user.getBrokenCount();
        restrictionCount = user.getRestrictionCount();
        reportCount = user.getReportCount();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
    }
}
