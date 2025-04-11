package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User{
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String studentNumber;

    //닉네임은 엑셀파일에서 가져올 수 없으므로 기본값 = 이름
    @Column
    private String nickname;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePicture;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private int restrictionCount;

    @Column(nullable = false)
    private int reportCount;

    @Column(nullable = false)
    private String role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // @OneToMany(mappedBy = "user")
    // private List<Equipment> equipments = new ArrayList<>();
    @Column
    private String equipmentId;

    // @OneToMany(mappedBy = "user")
    // private List<Bookmark> bookmarks = new ArrayList<>();
    @Column
    private String bookmarkId;

    // @OneToMany(mappedBy = "user")
    // private List<Complaint> complaints = new ArrayList<>();
    @Column
    private String complaintId;

    // @OneToMany(mappedBy = "user")
    // private List<RentalHistory> rentalHistories = new ArrayList<>();
    @Column
    private String rentalHistoryId;

    @Builder
    public User(String id, String name, String nickname, String department, String major, String grade, 
               String gender, String professor, String phoneNumber, String email, String password,
               String profilePicture, LocalDate birthDate, int restrictionCount, int reportCount,
               String role, LocalDateTime createdAt, LocalDateTime updatedAt, String equipmentId,
               String bookmarkId, String complaintId, String rentalHistoryId) {
        this.id = id;
        this.name = name;
        //엔티티 생성 시 기본값은 사용자 이름으로
        this.nickname = nickname != null ? nickname : name;
        this.department = department;
        this.major = major;
        this.grade = grade;
        this.gender = gender;
        this.professor = professor;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.birthDate = birthDate;
        this.restrictionCount = restrictionCount;
        this.reportCount = reportCount;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.equipmentId = equipmentId;
        this.bookmarkId = bookmarkId;
        this.complaintId = complaintId;
        this.rentalHistoryId = rentalHistoryId;
    }

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


} 