package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String studentNumber;

    //닉네임은 엑셀파일에서 가져올 수 없으므로 기본값 = 이름
    @Column
    private String nickname;

    @Column
    private String department;

    @Column
<<<<<<< HEAD
    private Integer grade;
=======
    private String major;

    @Column
    private int grade;
>>>>>>> 4207cc8f4009a2b9ab5292ee9418a038abef3b6c

    @Column(name = "\"group\"")
    private String group;

    @Column
    private String gender;

    @Column
    private String professor;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String profilePicture;

    @Column
    private LocalDate birthDate;

    @Column
    private int rentalCount;

    @Column
    private int damageCount;

    @Column
    private int restrictionCount;

    @Column
    private int reportCount;

    @Column
    private Role role;


    // @OneToMany(mappedBy = "user")
    // private List<Equipment> equipments = new ArrayList<>();
    @Column
    private String equipmentId;


    // @OneToMany(mappedBy = "user")
    // private List<Complaint> complaints = new ArrayList<>();
    @Column
    private String complaintId;

    // @OneToMany(mappedBy = "user")
    // private List<RentalHistory> rentalHistories = new ArrayList<>();
    @Column
    private String rentalHistoryId;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    @PrePersist //닉네임을 이름으로 설정하기 위해 30줄가량 생성자를 일일이 써야하는 문제를 해결
    public void prePersist() {
        
        if (this.nickname == null && this.name != null) {
            this.nickname = this.name;
        }
    }
} 