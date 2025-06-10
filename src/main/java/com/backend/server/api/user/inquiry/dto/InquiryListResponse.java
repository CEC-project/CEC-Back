package com.backend.server.api.user.inquiry.dto;

import com.backend.server.api.common.dto.PageableInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InquiryListResponse {

    @Schema(description = "문의글 리스트")
    private List<InquiryResponse> content;

    @Schema(description = "페이지네이션 정보")
    private PageableInfo pageable; //
}
