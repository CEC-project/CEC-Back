package com.backend.server.api.user.history.dto;

import com.backend.server.api.common.dto.PageableInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalRestrictionListResponse {
    private List<RentalRestrictionResponse> content;
    private PageableInfo pageable;
}
