package com.backend.server.api.user.inquiry.dto;

import com.backend.server.api.common.dto.AuthorResponse; // 공통 DTO로 변경
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryAnswerResponse {

    private String content;

    private AuthorResponse author; // 기존 자체 정의된 author → 공통 DTO로 교체
}
