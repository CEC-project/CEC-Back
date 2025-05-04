package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inquiry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;  // 문의 제목

    @Lob
    @Column(nullable = false)
    private String content;  // 문의 내용

    @Column(name = "author_id", nullable = false)
    private Long authorId;  // 작성자 ID

    @ManyToMany
    @JoinTable(
            name = "inquiry_inquiry_type",
            joinColumns = @JoinColumn(name = "inquiry_id"),
            inverseJoinColumns = @JoinColumn(name = "inquiry_type_id")
    )
    @Default
    private List<InquiryType> inquiryTypes = new ArrayList<>();  // 문의 유형 ID 리스트

    @Column
    private String attachmentUrl;  // 첨부파일 URL
}
