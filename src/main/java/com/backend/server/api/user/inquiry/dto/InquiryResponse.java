package com.backend.server.api.user.inquiry.dto;

import com.backend.server.model.entity.enums.InquiryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryResponse {

    private Long id;               // 문의 ID
    private String title;           // 제목
    private String content;         // 내용
    private String attachmentUrl;   // 첨부파일 URL
    private InquiryType type;       // 문의 유형
    private String createdAt;       // 생성 일시
}