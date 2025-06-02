package com.backend.server.api.admin.user.dto;

import com.backend.server.api.admin.user.dto.AdminUserListRequest.Gender;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class AdminUserRequest {
    @Schema(defaultValue = "1", description = "학년")
    private Integer grade;

    private Gender gender;

    @Schema(defaultValue = "202301234")
    @NotEmpty
    private String studentNumber;

    @NotEmpty
    private String name;
    private String nickname;

    @Schema(defaultValue = "1", implementation = Integer.class, description = "존재하는 지도교수만 입력가능.")
    private Long professorId;

    @NotEmpty
    private String birthday;

    @Schema(defaultValue = "01012341234")
    private String phoneNumber;
    private String profilePicture;

    public User toEntity(Professor professor, Role role, PasswordEncoder encoder) {
        return User.builder()
                .grade(grade)
                .gender(gender.name())
                .studentNumber(studentNumber)
                .name(name)
                .nickname(nickname)
                .professor(professor)
                .password(encoder.encode(studentNumber))
                .profilePicture(profilePicture)
                .birthDate(parseBirthday())
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }

    public LocalDate parseBirthday() {
        return LocalDate.parse(birthday);
    }
}
