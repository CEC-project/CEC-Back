package com.backend.server.api.common.inquiry.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryTypeResponse {

    private Long id; // id
    private String name; // 문의 유형 이름
}
