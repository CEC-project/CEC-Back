package com.backend.server.api.user.inquiry.dto;


import com.backend.server.model.entity.enums.InquiryType;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title; // 제목

    @NotBlank(message = "내용은 필수입니다.")
    private String content; // 내용

    private String attachmentUrl; // 첨부파일 URL (선택)

    @NotNull(message = "문의 유형을 선택하세요.")
    private InquiryType type; // 문의 유형 (enum)

}