package com.backend.server.api.common.dto;

import com.backend.server.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginUser {
    private Long id;
    private String name;
    private String studentNumber;
    private String nickname;
    private int grade;
    private String group;
    private String gender;
    private String professor;
    private String phoneNumber;
    private String email;
    private String profilePicture;
    private int rentalCount;
    private int damageCount;
    private int restrictionCount;
    private int reportCount;
    private String role;

    public LoginUser(User user) {
        id = user.getId();
        name = user.getName();
        studentNumber = user.getStudentNumber();
        nickname = user.getNickname();
        grade = user.getGrade();
        group = user.getGroup();
        gender = user.getGender();
        professor = user.getProfessor() == null ? "" : user.getProfessor().getName();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        profilePicture = user.getProfilePicture();
        rentalCount = user.getRentalCount();
        damageCount = user.getDamageCount();
        restrictionCount = user.getRestrictionCount();
        reportCount = user.getReportCount();
        role = user.getRole().toString();
    }
}