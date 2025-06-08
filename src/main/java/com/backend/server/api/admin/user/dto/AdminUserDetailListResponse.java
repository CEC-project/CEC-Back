package com.backend.server.api.admin.user.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class AdminUserDetailListResponse {
    private List<AdminUserDetailResponse> content;
    private PageableInfo pageable;

    public AdminUserDetailListResponse(Page<User> page, List<Professor> professors) {
        this.content = IntStream.range(0, page.getNumberOfElements())
                .mapToObj((i) -> new AdminUserDetailResponse(page.getContent().get(i), professors.get(i)))
                .toList();
        this.pageable = new PageableInfo(page);
    }
}
