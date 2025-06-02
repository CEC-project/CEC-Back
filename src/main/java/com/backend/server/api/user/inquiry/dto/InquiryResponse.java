package com.backend.server.api.user.inquiry.dto;

import com.backend.server.model.entity.enums.AnswerStatus;
import com.backend.server.model.entity.enums.InquiryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryResponse {

    @Schema(description = "문의 ID", example = "1")
    private Long id;               // 문의 ID

    @Schema(description = "문의 제목", example = "장비 대여 관련 문의")
    private String title;           // 제목

    @Schema(description = "문의 내용", example = "장비 대여 예약이 되지 않습니다.")
    private String content;         // 내용

    @Schema(description = "첨부파일 URL", example = "https://example.com/image.png")
    private String attachmentUrl;   // 첨부파일 URL

    @Schema(description = "문의 유형", example = "RENTAL")
    private InquiryType type;      // 문의 유형 (enum)

    @Schema(description = "답변 상태", example = "WAITING")
    private AnswerStatus status;   // 답변 상태

    @Schema(description = "문의 작성 시간 (ISO-8601)", example = "2025-06-02T15:34:21")
    private String createdAt;       // 생성 일시
}