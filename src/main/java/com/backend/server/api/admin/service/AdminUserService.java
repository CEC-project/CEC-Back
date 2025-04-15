package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.user.AdminUserListRequest;
import com.backend.server.api.admin.dto.user.AdminUserListResponse;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserListResponse getUsers(AdminUserListRequest request) {
        Pageable pageable = UserSpecification.getPageable(request);
        Specification<User> spec = UserSpecification.filterUsers(request);

        Page<User> page = userRepository.findAll(spec, pageable);

        return new AdminUserListResponse(page);
    }
}