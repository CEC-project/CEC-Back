package com.backend.server.model.entity;

import com.backend.server.model.entity.enums.AnswerStatus;
import com.backend.server.model.entity.enums.InquiryType;
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

    @Column(nullable = false)
    private String content;  // 문의 내용

    @Column(name = "author_id", nullable = false)
    private Long authorId;  // 작성자 ID

    @Column
    private String attachmentUrl;  // 첨부파일 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryType type;  // 문의 유형 (enum)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AnswerStatus status = AnswerStatus.WAITING; // 기본값 답변 대기 설정

    public void update(String title, String content, String attachmentUrl, InquiryType type){ // 수정
        this.title = title;
        this.content = content;
        this.attachmentUrl = attachmentUrl;
        this.type = type;
    }
}
