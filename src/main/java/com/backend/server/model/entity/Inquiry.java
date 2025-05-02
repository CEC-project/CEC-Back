package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Column(name = "author_id", nullable = false)
    private Long authorId;  // 작성자 ID

    @ElementCollection
    @CollectionTable(name = "inquiry_inquiry_type", joinColumns = @JoinColumn(name = "inquiry_id"))
    @Column(name = "inquiry_type_id")
    private List<Long> inquiryTypeIds = new ArrayList<>();  // 문의 유형 ID 리스트

    @Column
    private String attachmentUrl;  // 첨부파일 URL

    @Builder
    public Inquiry(String title, String content, String attachmentUrl, Long authorId, List<Long> inquiryTypeIds) {
        this.title = title;
        this.content = content;
        this.attachmentUrl = attachmentUrl;
        this.authorId = authorId;
        this.inquiryTypeIds = inquiryTypeIds;
    }
}
