package com.backend.server.api.admin.user.dto;

import com.backend.server.model.entity.User;
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
    private String gender;
    private String professor;
    private String phoneNumber;
    private String email;
    private String profilePicture;
    private LocalDate birthDate;
    private int rentalCount;
    private int damageCount;
    private int restrictionCount;
    private int reportCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public AdminUserResponse(User user) {
        id = user.getId();
        studentNumber = user.getStudentNumber();
        name = user.getName();
        nickname = user.getNickname();
        year = user.getGrade();
        gender = user.getGender();
        professor = user.getProfessor().getName();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        profilePicture = user.getProfilePicture();
        birthDate = user.getBirthDate();
        rentalCount = user.getRentalCount();
        damageCount = user.getDamageCount();
        restrictionCount = user.getRestrictionCount();
        reportCount = user.getReportCount();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
    }
}
