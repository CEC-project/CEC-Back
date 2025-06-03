package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListRequest.AdminUserSearchType;
import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class AdminInquiryListRequest extends AbstractPaginationParam {
    private String searchKeyword;
    private AdminUserSearchType searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber|3:nickname|4 or 생략:all)
    private Integer grade; // 1 or 2 or 3 or 4 or null
    private Gender gender; // '남' or '여' or null
    private Long professorId; // 교수 테이블의 id or null
    private Boolean answered; // 답변 여부 (생략시 여태까지 모든 문의가 조회됩니다.) // true or false or 생략
    private AdminInquirySortType sortBy;

    public AdminUserListRequest toAdminUserListRequest() {
        return AdminUserListRequest.builder()
                .searchKeyword(searchKeyword)
                .searchType(searchType)
                .gender(gender)
                .grade(grade)
                .professorId(professorId)
                .build();
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, sortDirection, sortBy.getField());
    }
}