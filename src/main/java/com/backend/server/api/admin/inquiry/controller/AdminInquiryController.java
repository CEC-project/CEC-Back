package com.backend.server.api.admin.inquiry.controller;

import com.backend.server.api.admin.inquiry.dto.AdminInquiryAnswerRequest;
import com.backend.server.api.admin.inquiry.dto.AdminInquiryListRequest;
import com.backend.server.api.admin.inquiry.dto.AdminInquiryListResponse;
import com.backend.server.api.admin.inquiry.service.AdminInquiryService;
import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/inquiry")
@RequiredArgsConstructor
@Tag(name = "문의 관리 API", description = "문의 관리 어드민 API")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @Operation(
            summary = "문의 목록 조회 API",
            description = """
            문의 목록을 조회합니다. 아래 조건들을 조합하여 필터링할 수 있습니다.
            
            - **검색**
                - **searchKeyword**: 검색 키워드입니다. **searchType**과 함께 사용됩니다. 생략 시 전체 조회됩니다.
                - **searchType**: 검색 대상 유형입니다. 다음 중 하나의 숫자를 입력해야 합니다. 생략 시 전체 조회됩니다.
                  - 0: 이름
                  - 1: 전화번호
                  - 2: 학번
                  - 3: 닉네임
                  - 4: 전체
            - **필터**
                - **grade**: 학년입니다. 1, 2, 3, 4 중 하나 또는 생략 가능하며, 생략 시 전체 조회됩니다.
                - **gender**: 성별입니다. "남" 또는 "여" 중 하나를 입력할 수 있습니다. 생략 시 전체 조회됩니다.
                - **professorId**: 담당 교수의 ID입니다. 생략 시 전체 조회됩니다.
                - **answered**: 답변 여부입니다. true / false 생략시 전체 조회됩니다.
            - **페이지네이션**
                - **page**: 요청 페이지 번호입니다. 기본값은 0입니다.
                - **size**: 한 페이지에 포함될 데이터 개수입니다. 기본값은 10입니다.
            - **정렬**
                - **sortBy**: 정렬 기준입니다. 다음 중 하나를 선택할 수 있습니다.
                  - NAME (이름 기준 정렬)
                  - STUDENT_NUMBER (학번 기준 정렬)
                  - CREATION_TIME (문의 생성일 기준 정렬)
                - **direction**: 정렬 순서입니다. ASC(오름차순) 또는 DESC(내림차순) 중 하나를 선택할 수 있습니다. 기본값은 ASC입니다.
            
            **⚠️ answered 필터를 설정해야 답변 해야할 문의 목록을 조회가능함**""")
    @GetMapping("")
    public ApiResponse<AdminInquiryListResponse> getInquiries(@ParameterObject AdminInquiryListRequest request) {
        AdminInquiryListResponse result = adminInquiryService.getInquiries(request);
        return ApiResponse.success("문의 목록 조회 성공", result);
    }

    @Operation(
            summary = "문의 답변 등록 API",
            description = """
            - **패스 파라미터**
              - **inquiryId** : 답변할 문의 id (필수)
            - **요청 바디**
              - **$.content** : 답변 내용 (빈값 불가) (1000자 제한)""")
    @PostMapping("/{inquiryId}")
    public ApiResponse<Long> addResponse(
            @PathVariable Long inquiryId,
            @Valid @RequestBody AdminInquiryAnswerRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        Long responderId = loginUser.getId();
        Long id = adminInquiryService.addResponse(inquiryId, request, responderId);
        return ApiResponse.success("문의 답변 등록 성공", id);
    }
}