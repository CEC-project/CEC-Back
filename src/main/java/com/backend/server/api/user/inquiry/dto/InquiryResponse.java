package com.backend.server.api.user.inquiry.dto;

import com.backend.server.model.entity.enums.AnswerStatus;
import com.backend.server.model.entity.enums.InquiryType;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "문의 ID", example = "1")
    private Long id;

    @Schema(description = "문의 제목", example = "장비 대여 관련 문의")
    private String title;

    @Schema(description = "문의 내용", example = "장비 대여 예약이 되지 않습니다.")
    private String content;

    @Schema(description = "첨부파일 목록")
    private List<String> attachments;

    @Schema(description = "문의 유형", example = "RENTAL")
    private InquiryType type;

    @Schema(description = "답변 상태", example = "NOT_ANSWERED")
    private AnswerStatus status;

    @Schema(description = "문의 작성 시간")
    private String createdAt;

    @Schema(description = "답변 내용")
    private InquiryAnswerResponse answer; // answer.author → 공통 AuthorResponse 사용
}
