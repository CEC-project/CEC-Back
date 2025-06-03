package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListRequest.AdminUserSearchType;
import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.Gender;
import com.backend.server.model.entity.enums.RestrictionReason;
import com.backend.server.model.entity.enums.RestrictionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class AdminRentalRestrictionListRequest extends AbstractPaginationParam {
    private String searchKeyword;
    private AdminUserSearchType searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber|3:nickname|4 or 생략:all)
    private Integer grade; // 1 or 2 or 3 or 4 or null
    private Gender gender; // '남' or '여' or null
    private RestrictionType type;
    private RestrictionReason reason;
    private Long professorId; // 교수 테이블의 id or null
    private AdminRentalRestrictionSortType sortBy;

    public Pageable toPageable(boolean isUserTable) {
        if (isUserTable) {
            String field = sortBy.isUserTable() ? sortBy.getField() : "rentalRestriction." + sortBy.getField();
            return PageRequest.of(page, size, sortDirection, field);
        }
        String field = sortBy.isUserTable() ? "user." + sortBy.getField() : sortBy.getField();
        return PageRequest.of(page, size, sortDirection, field);
    }

    public AdminUserListRequest toAdminUserListRequest() {
        return AdminUserListRequest.builder()
                .searchKeyword(searchKeyword)
                .searchType(searchType)
                .gender(gender)
                .grade(grade)
                .professorId(professorId)
                .build();
    }
}