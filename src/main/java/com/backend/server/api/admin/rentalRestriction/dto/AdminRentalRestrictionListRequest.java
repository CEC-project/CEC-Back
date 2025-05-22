package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.api.common.dto.pagination.AbstractPaginationParam;
import com.backend.server.model.entity.enums.RestrictionReason;
import com.backend.server.model.entity.enums.RestrictionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRentalRestrictionListRequest extends AbstractPaginationParam<AdminRentalRestrictionSortType> {
    private String searchKeyword;
    private Integer searchType; // 검색 유형 (0:name|1:phoneNumber|2:studentNumber)
    private Integer grade; // 1 or 2 or 3 or 4 or null
    private String gender; // '남' or '여' or null
    private RestrictionType type;
    private RestrictionReason reason;
    private Long professorId; // 교수 테이블의 id or null
}