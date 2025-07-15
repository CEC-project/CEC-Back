package com.backend.server.fixture;

import com.backend.server.api.admin.user.dto.AdminUserRequest;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Gender;
import com.backend.server.model.entity.enums.Role;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Getter
public enum UserFixture {
    MOCK_MVC_테스트시_로그인_계정(
            "테스트",
            "202011210",
            "테스트",
            "부서",
            1,
            "전공1",
            "반1",
            Gender.M,
            "01012341234",
            "asd@asdf.com",
            "202011210",
            null,
            LocalDate.of(2020, 1, 1),
            0,
            0,
            0,
            Role.ROLE_SUPER_ADMIN,
            1L
    ),
    관리자1(
            "관리자1",
            "202011212",
            "관리자1닉",
            "부서",
            1,
            "전공1",
            "반1",
            Gender.M,
            "01012345678",
            "asd@asdf.com",
            "202011212",
            null,
            LocalDate.of(2020, 1, 1),
            0,
            0,
            0,
            Role.ROLE_SUPER_ADMIN,
            1L
    ),
    사용자1(
            "사용자1",
            "202011213",
            "사용자1닉",
            "부서",
            1,
            "전공1",
            "반1",
            Gender.F,
            "01012345678",
            "asd@asdf.com",
            "202011212",
            null,
            LocalDate.of(2020, 1, 1),
            0,
            0,
            0,
            Role.ROLE_USER,
            1L
            );

    private final String name;
    private final String studentNumber;
    private final String nickname;
    private final String department;
    private final Integer grade;
    private final String major;
    private final String group;
    private final Gender gender;
    private final String phoneNumber;
    private final String email;
    private final String password;
    private final String profilePicture;
    private final LocalDate birthDate;
    private final Integer rentalCount;
    private final Integer brokenCount;
    private final Integer restrictionCount;
    private final Role role;
    private final Long professorId;

    public User 엔티티_생성(PasswordEncoder passwordEncoder, Professor professor) {
        return User.builder()
                .name(name)
                .studentNumber(studentNumber)
                .nickname(nickname)
                .department(department)
                .grade(grade)
                .major(major)
                .group(group)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .email(email)
                .password(passwordEncoder.encode(password))
                .profilePicture(profilePicture)
                .birthDate(birthDate)
                .rentalCount(rentalCount)
                .brokenCount(brokenCount)
                .restrictionCount(restrictionCount)
                .role(role)
                .professor(professor)
                .build();
    }

    public AdminUserRequest 등록_요청_생성(Long professorId) {
        return AdminUserRequest.builder()
                .name(name)
                .studentNumber(studentNumber)
                .nickname(nickname)
                .grade(grade)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .professorId(professorId)
                .profilePicture(profilePicture)
                .birthday(birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
