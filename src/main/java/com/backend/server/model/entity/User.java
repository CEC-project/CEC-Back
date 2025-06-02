package com.backend.server.model.entity;

import com.backend.server.api.admin.user.dto.AdminUserRequest;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import java.time.LocalDate;

import com.backend.server.model.entity.enums.Role;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String studentNumber;

    //닉네임은 엑셀파일에서 가져올 수 없으므로 기본값 = 이름
    @Column(nullable = false)
    private String nickname;

    @Column
    private String department;

    @Column(nullable = false)
    private Integer grade;

    @Column
    private String major;

    @Column(name = "\"group\"")
    private String group;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String profilePicture;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column
    private int rentalCount;

    @Column
    private int damageCount;

    @Column
    private int restrictionCount;

    @Column
    private int reportCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Professor professor;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RentalRestriction> rentalRestrictions = new ArrayList<>();

    @PrePersist //닉네임을 이름으로 설정하기 위해 30줄가량 생성자를 일일이 써야하는 문제를 해결
    public void prePersist() {

        if (this.nickname == null && this.name != null) {
            this.nickname = this.name;
        }
    }

    public void update(Professor professor, AdminUserRequest request) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.studentNumber = request.getStudentNumber();
        this.grade = request.getGrade();
        this.gender = request.getGender();
        this.phoneNumber = request.getPhoneNumber();
        this.professor = professor;
        this.birthDate = request.parseBirthday();
        this.profilePicture = request.getProfilePicture();
    }
}