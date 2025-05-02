package com.backend.server.api.admin.dto.user;

import com.backend.server.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class AdminUserRequest {
    private String department;
    private Integer grade;
    private String gender;
    @Schema(defaultValue = "202301234")
    private String studentNumber;
    private String name;
    private String nickname;
    @Schema(defaultValue = "유응구")
    private String professor;
    @Schema(defaultValue = "2024-01-01")
    private String birthday;
    @Schema(defaultValue = "01012341234")
    private String phoneNumber;
    private String profilePicture;

    public User toEntity(Long id, PasswordEncoder encoder) {
        LocalDate date = null;

        if (birthday != null && !birthday.isEmpty()) {
            try {
                date = LocalDate.parse(birthday);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("생일 형식이 올바르지 않습니다");
            }
        }

        return User.builder()
                .id(id)
                .department(department)
                .grade(grade)
                .gender(gender)
                .studentNumber(studentNumber)
                .name(name)
                .nickname(nickname)
                .professor(professor)
                .password(encoder.encode(studentNumber))
                .profilePicture(profilePicture)
                .birthDate(date)
                .phoneNumber(phoneNumber)
                .build();
    }
}
