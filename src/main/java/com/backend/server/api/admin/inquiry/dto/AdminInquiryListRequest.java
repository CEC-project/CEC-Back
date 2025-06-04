package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListRequest.AdminUserSearchType;
import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.Gender;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class AdminInquiryListRequest extends AbstractPaginationParam {
    public enum AdminInquirySearchType {
        NAME, PHONE_NUMBER, STUDENT_NUMBER, NICKNAME, CONTENT, ALL;
        public Optional<AdminUserSearchType> toAdminUserSearchType() {
            return Arrays.stream(AdminUserSearchType.values())
                    .filter(t -> t.name().equals(name()))
                    .findFirst();
        }
    }

    private String searchKeyword;
    private AdminInquirySearchType searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber|3:nickname|4 or 생략:all)
    private Integer grade; // 1 or 2 or 3 or 4 or null
    private Gender gender; // '남' or '여' or null
    private Long professorId; // 교수 테이블의 id or null
    private Boolean answered; // 답변 여부 (생략시 여태까지 모든 문의가 조회됩니다.) // true or false or 생략
    private AdminInquirySortType sortBy;

    public Optional<AdminUserListRequest> toAdminUserListRequest() {
        Optional<AdminUserSearchType> newSearchType = searchType.toAdminUserSearchType();
        if (newSearchType.isEmpty())
            return Optional.empty();
        AdminUserListRequest result = AdminUserListRequest.builder()
                .searchKeyword(searchKeyword)
                .searchType(newSearchType.get())
                .gender(gender)
                .grade(grade)
                .professorId(professorId)
                .build();
        return Optional.of(result);
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, sortDirection, sortBy.getField());
    }
}