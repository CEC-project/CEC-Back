package com.backend.server.api.user.history.dto;

import com.backend.server.api.common.dto.PageableInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalHistoryListResponse {
    private List<RentalHistoryResponse> content;
    private PageableInfo pageable;
}
