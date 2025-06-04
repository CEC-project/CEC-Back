package com.backend.server.api.user.inquiry.dto;


import com.backend.server.model.entity.enums.InquiryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {


    @Schema(description = "문의 제목", example = "장비 대여 관련 문의")
    @NotBlank(message = "제목은 필수입니다.")
    private String title; // 제목

    @Schema(description = "문의 내용", example = "장비 대여 예약이 되지 않습니다.")
    @NotBlank(message = "내용은 필수입니다.")
    private String content; // 내용

    @Schema(description = "첨부파일 URL", example = "https://example.com/image.png")
    private String attachmentUrl; // 첨부파일 URL (선택)

    @Schema(description = "문의 유형", example = "RENTAL")
    @NotNull(message = "문의 유형을 선택하세요.")
    private InquiryType type; // 문의 유형 (enum)

}