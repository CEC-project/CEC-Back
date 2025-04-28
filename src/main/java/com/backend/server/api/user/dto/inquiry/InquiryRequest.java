package com.backend.server.api.user.dto.inquiry;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequest {

    private String title;          // 제목
    private String content;        // 내용
    private String attachmentUrl;  // 첨부파일 URL (선택)
    private List<Long> inquiryTypeIds; // 문의 유형 ID 리스트

}