package com.backend.server.api.user.inquiry.dto;


import com.backend.server.model.entity.enums.InquiryType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {

    private String title;          // 제목
    private String content;        // 내용
    private String attachmentUrl;  // 첨부파일 URL (선택)
    private InquiryType type;;     // 문의 유형


}