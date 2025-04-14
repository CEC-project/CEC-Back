package com.backend.server.api.admin.dto.user;

import com.backend.server.api.dto.PageableInfo;
import com.backend.server.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminUserListResponse {
    private List<AdminUserResponse> content;
    private PageableInfo pageable;

    public AdminUserListResponse(Page<User> page) {
        this.content = page.getContent().stream().map(AdminUserResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
