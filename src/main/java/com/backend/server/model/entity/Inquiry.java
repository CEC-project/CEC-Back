package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inquiry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;  // 문의 제목

    @Lob
    @Column(nullable = false)
    private String content;  // 문의 내용

    @Column(nullable = false)
    private Boolean secret;  // 비공개 여부

    @Column(name = "author_id", nullable = false)
    private Long authorId;  // 작성자 ID (User를 직접 참조하지 않음)

    @ElementCollection
    @CollectionTable(name = "inquiry_inquiry_type", joinColumns = @JoinColumn(name = "inquiry_id"))
    @Column(name = "inquiry_type_id")
    private List<Long> inquiryTypeIds = new ArrayList<>();  // 문의 유형 id만 저장 (InquiryType과 직접 연결 X)
}