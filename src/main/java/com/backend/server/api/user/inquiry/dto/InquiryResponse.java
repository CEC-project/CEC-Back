package com.backend.server.api.user.inquiry.dto;

import com.backend.server.model.entity.enums.AnswerStatus;
import com.backend.server.model.entity.enums.InquiryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "문의 조회 응답 DTO")
public class InquiryResponse {

    @Schema(description = "문의 ID", example = "1")
    private Long id;

    @Schema(description = "문의 제목", example = "장비 대여 관련 문의")
    private String title;

    @Schema(description = "문의 내용", example = "장비 대여 가능 시간에 대해 알고 싶습니다.")
    private String content;

    @Schema(description = "첨부 파일 URL", example = "https://example.com/file.png")
    private String attachmentUrl;

    @Schema(description = "문의 유형", example = "RENTAL", implementation = InquiryType.class)
    private InquiryType type;

    @Schema(description = "답변 상태", example = "WAITING", implementation = AnswerStatus.class)
    private AnswerStatus status;

    @Schema(description = "문의 작성 시각 (yyyy-MM-dd HH:mm:ss)", example = "2025-06-03 15:12:00")
    private String createdAt;
}
