package com.backend.server.api.admin.user.dto;

import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class AdminUserRequest {
    @NotEmpty
    private Integer grade;

    @Schema(defaultValue = "남", description = "성별은 '남' 또는 '여'만 입력 가능합니다.")
    @Pattern(regexp = "[남여]", message = "성별은 '남' 또는 '여'만 입력 가능합니다.")
    private String gender;

    @Schema(defaultValue = "202301234")
    @NotEmpty
    private String studentNumber;

    @NotEmpty
    private String name;
    private String nickname;
    private Long professorId;

    @Schema(defaultValue = "2024-01-01", description = "생일은 yyyy-MM-dd 형식이어야 합니다.")
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "생일은 yyyy-MM-dd 형식이어야 합니다.")
    @NotEmpty
    private String birthday;

    @Schema(defaultValue = "01012341234")
    private String phoneNumber;
    private String profilePicture;

    public User toEntity(Professor professor, PasswordEncoder encoder) {
        return User.builder()
                .grade(grade)
                .gender(gender)
                .studentNumber(studentNumber)
                .name(name)
                .nickname(nickname)
                .professor(professor)
                .password(encoder.encode(studentNumber))
                .profilePicture(profilePicture)
                .birthDate(parseBirthday())
                .phoneNumber(phoneNumber)
                .build();
    }

    public LocalDate parseBirthday() {
        return LocalDate.parse(birthday);
    }
}
